package com.beryllinus.backend.service;

import com.beryllinus.backend.enumuration.RoomClassType;
import com.beryllinus.backend.exceptions.RoomClassConfigNotFoundException;
import com.beryllinus.backend.exceptions.RoomConfigNotFoundException;
import com.beryllinus.backend.exceptions.RoomNotFoundException;
import com.beryllinus.backend.model.room.*;
import com.beryllinus.backend.repository.RoomClassConfigRepository;
import com.beryllinus.backend.repository.RoomClassRepository;
import com.beryllinus.backend.repository.RoomSettingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RoomSettingService {

    private final RoomService roomService;
    private final RoomClassConfigRepository roomClassConfigRepository;
    private final RoomClassRepository roomClassRepository;
    private final RoomSettingRepository roomSettingRepository;

    public RoomSettingService(
            RoomClassConfigRepository roomClassConfigRepository,
            RoomService roomService,
            RoomClassRepository roomClassRepository,
            RoomSettingRepository roomSettingRepository
    ) {
        this.roomClassConfigRepository = roomClassConfigRepository;
        this.roomService = roomService;
        this.roomClassRepository = roomClassRepository;
        this.roomSettingRepository = roomSettingRepository;
    }

    /**
     * Generate RoomSettings for all active RoomClasses for a given date.
     */
    public List<RoomSetting> generateRoomSettingList(final LocalDate date) {

        List<RoomClass> roomClassList =
                roomClassRepository.findAllByIsActive(true);

        List<RoomSetting> roomSettingList = new ArrayList<>();

        roomClassList.forEach(
                roomClass -> roomSettingList.add(
                        generateRoomSettings(roomClass, date)
                )
        );

        return roomSettingList;
    }

    /**
     * Generate a RoomSetting for a RoomClass on a specific date.
     */
    public RoomSetting generateRoomSettings(
            RoomClass roomClass,
            LocalDate date
    ) {

        RoomSetting roomSetting =
                new RoomSetting(roomClass, date);

        validateRoomSettingWithRoomClass(
                roomSetting,
                roomClass
        );

        validateRoomSettingWithRoomClassConfig(
                roomSetting,
                getRoomClassConfig(roomClass, date)
        );

        roomSetting.setAvailableRooms(
                calculateRoomAvailability(roomClass, date)
        );

        return roomSetting;
    }

    /**
     * Generate and persist RoomSettings.
     */
    @Transactional
    public List<RoomSetting> generateAndPersistRoomSettingList(
            final LocalDate date
    ) {

        List<RoomSetting> roomSettingList =
                generateRoomSettingList(date);

        return roomSettingRepository.saveAll(roomSettingList);
    }

    private void validateRoomSettingWithRoomClass(
            RoomSetting roomSetting,
            RoomClass roomClass
    ) {

        roomSetting.setRoomClass(roomClass);

        roomSetting.setBaseIsActive(roomClass.isActive());

        roomSetting.setBaseIsLocalBookingActive(
                roomClass.isLocalBookingActive()
        );

        roomSetting.setBasePriceLocal(
                roomClass.getPriceLocal()
        );

        roomSetting.setBasePriceLocalCurrency(
                roomClass.getPriceLocalCurrency()
        );

        roomSetting.setBaseIsInternationalBookingActive(
                roomClass.isInternationalBookingActive()
        );

        roomSetting.setBasePriceInternational(
                roomClass.getPriceInternational()
        );

        roomSetting.setBasePriceInternationalCurrency(
                roomClass.getPriceInternationalCurrency()
        );
    }

    private Optional<RoomClassConfig> getRoomClassConfig(
            RoomClass roomClass,
            LocalDate date
    ) {

        Optional<List<RoomClassConfig>> roomClassConfigList =
                roomClassConfigRepository
                        .findRoomsClassConfigByIdAndDateAndIsActive(
                                roomClass.getId(),
                                date
                        );

        if (roomClassConfigList.isEmpty()) {
            return Optional.empty();
        }

        if (roomClassConfigList.get().size() == 1) {
            return Optional.of(
                    roomClassConfigList.get().getFirst()
            );
        }

        return Optional.of(
                roomClassConfigList.get()
                        .stream()
                        .max(
                                Comparator.comparing(
                                        RoomClassConfig::getUpdatedAt
                                )
                        )
                        .orElseThrow(
                                RoomClassConfigNotFoundException::new
                        )
        );
    }

    private void validateRoomSettingWithRoomClassConfig(
            RoomSetting roomSetting,
            Optional<RoomClassConfig> roomClassConfig
    ) {

        if (roomClassConfig.isEmpty()) {

            roomSetting.setCalculatedIsActive(
                    roomSetting.isBaseIsActive()
            );

            roomSetting.setCalcPriceLocal(
                    roomSetting.getBasePriceLocal()
            );

            roomSetting.setCalcPriceLocalCurrency(
                    roomSetting.getBasePriceLocalCurrency()
            );

            roomSetting.setCalIsLocalBookingActive(
                    roomSetting.isBaseIsLocalBookingActive()
            );

            roomSetting.setCalcPriceInternational(
                    roomSetting.getBasePriceInternational()
            );

            roomSetting.setCalcPriceInternationalCurrency(
                    roomSetting.getBasePriceInternationalCurrency()
            );

            roomSetting.setCalcIsInternationalBookingActive(
                    roomSetting.isBaseIsInternationalBookingActive()
            );

            return;
        }

        RoomClassConfig config = roomClassConfig.get();

        roomSetting.setCalculatedIsActive(config.isActive());

        roomSetting.setCalcPriceLocal(config.getPriceLocal());
        roomSetting.setCalcPriceLocalCurrency(
                config.getPriceLocalCurrency()
        );

        roomSetting.setCalIsLocalBookingActive(
                config.isLocalBookingActive()
        );

        roomSetting.setCalcPriceInternational(
                config.getPriceInternational()
        );

        roomSetting.setCalcPriceInternationalCurrency(
                config.getPriceInternationalCurrency()
        );

        roomSetting.setCalcIsInternationalBookingActive(
                config.isInternationalBookingActive()
        );
    }

    private int calculateRoomAvailability(
            RoomClass roomClass,
            LocalDate date
    ) {

        int availableRooms = 0;

        try {
            for (Room room :
                    roomService.getAllActiveRoomsByRoomClass(roomClass)) {

                Optional<RoomConfig> configOpt =
                        roomService.getRoomConfigByDate(room, date);

                if (configOpt.isEmpty()
                        || configOpt.get().isActive()) {
                    availableRooms++;
                }
            }
        } catch (RoomNotFoundException | RoomConfigNotFoundException e) {
            throw new RuntimeException(e);
        }

        return availableRooms;
    }
    /**
     * Get all RoomSettings for a specific date.
     */
    public List<RoomSetting> getByDate(
            LocalDate date
    ) {

        return roomSettingRepository.findByDate(date);
    }

    /**
     * Get all RoomSettings within a date range.
     */
    public List<RoomSetting> getByDateRange(
            LocalDate startDate,
            LocalDate endDate
    ) {

        return roomSettingRepository.findByDateBetween(
                startDate,
                endDate
        );
    }

    /**
     * Get RoomSetting for a RoomClass and Date.
     */
    public RoomSetting getByRoomClassAndDate(
            RoomClassType roomClassType,
            LocalDate date
    ) {

        RoomClass roomClass =
                roomClassRepository
                        .findByRoomClassType(roomClassType)
                        .orElseThrow(RoomNotFoundException::new);

        return roomSettingRepository
                .findByRoomClassAndDate(
                        roomClass,
                        date
                )
                .orElseThrow(RoomNotFoundException::new);
    }

    /**
     * Get RoomSettings for a RoomClass within a date range.
     */
    public List<RoomSetting> getByRoomClassAndDateRange(
            RoomClassType roomClassType,
            LocalDate startDate,
            LocalDate endDate
    ) {

        RoomClass roomClass =
                roomClassRepository
                        .findByRoomClassType(roomClassType)
                        .orElseThrow(RoomNotFoundException::new);

        return roomSettingRepository
                .findByRoomClassAndDateBetween(
                        roomClass,
                        startDate,
                        endDate
                );
    }

}
