package com.beryllinus.backend.service;

import com.beryllinus.backend.dto.request.BookingPrecheckRequest;
import com.beryllinus.backend.dto.request.BookingRequest;
import com.beryllinus.backend.dto.response.BookingPrecheckResponse;
import com.beryllinus.backend.dto.response.BookingResponse;
import com.beryllinus.backend.dto.response.PriceResponse;
import com.beryllinus.backend.dto.response.RoomAvailabilityResponse;
import com.beryllinus.backend.enumuration.AggregateType;
import com.beryllinus.backend.enumuration.BookingStatus;
import com.beryllinus.backend.enumuration.Currency;
import com.beryllinus.backend.enumuration.EventType;
import com.beryllinus.backend.exceptions.BookingException;
import com.beryllinus.backend.mapper.BookingMapper;
import com.beryllinus.backend.model.Booking;
import com.beryllinus.backend.model.OutboxEvent;
import com.beryllinus.backend.model.room.RoomClass;
import com.beryllinus.backend.model.room.RoomSetting;
import com.beryllinus.backend.repository.BookingRepository;
import com.beryllinus.backend.repository.RoomSettingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class BookingService {
    private final RoomClassService roomClassService;
    private final RoomSettingService roomSettingService;
    private final RoomSettingRepository roomSettingRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final OutboxEventService outboxEventService;


    public BookingPrecheckResponse getPreCheckBooking(
            BookingPrecheckRequest bookingPrecheckRequest
    ) {

        List<RoomClass> roomClassList = new ArrayList<>();

        if (bookingPrecheckRequest.getRoomClassType() == null) {
            roomClassList.addAll(roomClassService.getAllActiveRoomClasses());
        } else {
            roomClassList.add(
                    roomClassService.getRoomClassByType(
                            bookingPrecheckRequest.getRoomClassType()
                    )
            );
        }

        List<RoomAvailabilityResponse> roomClasses = new ArrayList<>();

        for (RoomClass roomClass : roomClassList) {

            int minAvailableRooms = Integer.MAX_VALUE;

            BigDecimal highestLocalPrice = BigDecimal.ZERO;
            BigDecimal highestInternationalPrice = BigDecimal.ZERO;

            boolean available = true;

            LocalDate date = bookingPrecheckRequest.getCheckIn();

            while (date.isBefore(bookingPrecheckRequest.getCheckOut())) {

                RoomSetting roomSetting =
                        roomSettingService.getByRoomClassAndDate(
                                roomClass.getRoomClassType(),
                                date
                        );

                if (!roomSetting.isCalculatedIsActive()
                        || roomSetting.getAvailableRooms() < bookingPrecheckRequest.getRooms()) {

                    available = false;
                    break;
                }

                minAvailableRooms = Math.min(
                        minAvailableRooms,
                        roomSetting.getAvailableRooms()
                );

                highestLocalPrice = highestLocalPrice.max(
                        roomSetting.getCalcPriceLocal()
                );

                highestInternationalPrice = highestInternationalPrice.max(
                        roomSetting.getCalcPriceInternational()
                );

                date = date.plusDays(1);
            }

            if (!available) {
                continue;
            }

            RoomAvailabilityResponse.RoomAvailabilityResponseBuilder builder =
                    RoomAvailabilityResponse.builder()
                            .roomClassType(roomClass.getRoomClassType())
                            .availableRooms(minAvailableRooms);

            if (highestLocalPrice.compareTo(BigDecimal.ZERO) > 0) {
                builder.local(
                        PriceResponse.builder()
                                .pricePerNight(highestLocalPrice)
                                .currency("LKR")
                                .build()
                );
            }

            if (highestInternationalPrice.compareTo(BigDecimal.ZERO) > 0) {
                builder.international(
                        PriceResponse.builder()
                                .pricePerNight(highestInternationalPrice)
                                .currency("USD")
                                .build()
                );
            }

            roomClasses.add(builder.build());
        }

        return BookingPrecheckResponse.builder()
                .checkIn(bookingPrecheckRequest.getCheckIn())
                .checkOut(bookingPrecheckRequest.getCheckOut())
                .nights(
                        (int) ChronoUnit.DAYS.between(
                                bookingPrecheckRequest.getCheckIn(),
                                bookingPrecheckRequest.getCheckOut()
                        )
                )
                .roomClasses(roomClasses)
                .build();
    }

    @Transactional
    public BookingResponse createTemporaryBooking(
            BookingRequest request) {
        /*
         * Fetch active room class for the requested type.
         * RoomClass is cached so this should be very fast.
         */
        RoomClass roomClass = roomClassService
                .getRoomClassByType(request.roomClassType());

        /*
         * Lock all RoomSetting rows for the requested date range.
         */
        List<RoomSetting> roomSettings =
                roomSettingService.findForBookingWithLock(
                        roomClass.getId(),
                        request.checkIn(),
                        request.checkOut());

        /*
         * Verify room settings exist for every night.
         *
         * Example:
         * Jul 1 -> Jul 5 = 4 nights
         *
         * Expected:
         * Jul 1
         * Jul 2
         * Jul 3
         * Jul 4
         */
        long expectedDays = ChronoUnit.DAYS.between(
                request.checkIn(),
                request.checkOut());

        if (roomSettings.size() != expectedDays) {
            throw new BookingException(
                    "Room settings are not configured for the entire stay.");
        }

        /*
         * Validate every RoomSetting before reserving inventory.
         */
        for (RoomSetting roomSetting : roomSettings) {

            /*
             * Room class is closed for this day.
             */
            if (!roomSetting.isCalculatedIsActive()) {
                throw new BookingException(
                        roomSetting.getDate().toString());
            }

            /*
             * Validate booking channel.
             */
            if (request.internationalBooking()) {

                if (!roomSetting.isCalcIsInternationalBookingActive()) {
                    throw new BookingException(
                            roomSetting.getDate().toString());
                }

            } else {

                if (!roomSetting.isCalIsLocalBookingActive()) {
                    throw new BookingException(
                            roomSetting.getDate().toString());
                }
            }

            /*
             * Validate available inventory.
             *
             * Every day must satisfy the requested room count.
             */
            if (roomSetting.getAvailableRooms() < request.roomsBooked()) {
                throw new BookingException(
                        roomSetting.getDate().toString());
            }
        }

        /*
         * Determine nightly rate and currency.
         */
        BigDecimal pricePerNight;
        Currency currency;

        if (request.internationalBooking()) {

            pricePerNight = roomSettings.getFirst()
                    .getCalcPriceInternational();

            currency = roomSettings.getFirst()
                    .getCalcPriceInternationalCurrency();

        } else {

            pricePerNight = roomSettings.getFirst()
                    .getCalcPriceLocal();

            currency = roomSettings.getFirst()
                    .getCalcPriceLocalCurrency();
        }

        /*
         * Calculate total amount.
         *
         * total =
         * nightlyRate × roomsBooked × nights
         */
        BigDecimal totalAmount = pricePerNight
                .multiply(BigDecimal.valueOf(request.roomsBooked()))
                .multiply(BigDecimal.valueOf(expectedDays));

        /*
         * Reserve inventory while rows remain locked.
         *
         * Example:
         * Available = 10
         * Requested = 2
         * Remaining = 8
         */
        roomSettings.forEach(roomSetting ->
                roomSetting.setAvailableRooms(
                        roomSetting.getAvailableRooms()
                                - request.roomsBooked()));

        /*
         * Persist updated inventory.
         */
        roomSettingRepository.saveAll(roomSettings);

        /*
         * Create temporary booking.
         *
         * expiresAt will be automatically set
         * by Booking.prePersist().
         */
        Booking booking = Booking.builder()
                .roomClass(roomClass)
                .checkIn(request.checkIn())
                .checkOut(request.checkOut())
                .roomsBooked(request.roomsBooked())
                .status(BookingStatus.TEMPORARY)
                .isInternationalBooking(
                        request.internationalBooking())
                .pricePerNight(pricePerNight)
                .totalAmount(totalAmount)
                .currency(currency)
                .specialRequests(request.specialRequests())
                .build();

        /*
         * Persist booking.
         */
        booking = bookingRepository.save(booking);

        // Add Message to Outbox to kafka to proceed
        outboxEventService.addOutboxEvent(OutboxEvent.builder()
                .aggregateType(AggregateType.BOOKING)
                .aggregateId(booking.getId())
                .eventType(EventType.TEMPORARY_BOOKING_CREATED)
                .payload("""
                        {
                          "bookingId":"%s",
                          "status":"TEMPORARY"
                        }
                        """.formatted(booking.getId()))
                .published(false)
                .build());

        /*
         * Convert entity to API response.
         */
        return bookingMapper.toResponse(booking);
    }
}
