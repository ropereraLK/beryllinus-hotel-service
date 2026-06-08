package com.beryllinus.hotel_service.service;

import com.beryllinus.hotel_service.enumuration.Currency;
import com.beryllinus.hotel_service.exceptions.RoomClassConfigNotFoundException;
import com.beryllinus.hotel_service.exceptions.RoomConfigNotFoundException;
import com.beryllinus.hotel_service.exceptions.RoomNotFoundException;
import com.beryllinus.hotel_service.model.room.*;
import com.beryllinus.hotel_service.repository.RoomClassConfigRepository;
import com.beryllinus.hotel_service.repository.RoomConfigRepository;
import com.beryllinus.hotel_service.repository.RoomRepository;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class RoomSettingsService {

    private final RoomRepository roomRepository;
    private final RoomClassConfigRepository roomClassConfigRepository;
    private final RoomService roomService;

    public RoomSettingsService(RoomRepository roomRepository,
                               RoomClassConfigRepository roomClassConfigRepository,
                               RoomService roomService) {

        this.roomRepository = roomRepository;
        this.roomClassConfigRepository = roomClassConfigRepository;
        this.roomService = roomService;
    }

    /**
     * @param roomClass: roomClass
     *                   By the repository layer only active roomClasses are fetched
     */
    public RoomSetting validateRoomSettings(RoomClass roomClass, LocalDate date) throws RoomNotFoundException {
        //Create the new RoomSetting
        RoomSetting roomSetting = new RoomSetting(roomClass.getId());

        //Validate RoomSetting with RoomClass
        validateRoomSettingWithRoomClass(roomSetting, roomClass);

        try {
            validateRoomSettingWithRoomClassConfig(roomSetting, getRoomClasConfig(roomClass, date));

        } catch (RoomClassConfigNotFoundException e) {
            //No Room Config Found, Use Room Base Data
            //TODO: Log this
        }

        roomService.getAllActiveRooms();

        return roomSetting;
    }

    private void validateRoomSettingWithRoomClass(RoomSetting roomSetting, RoomClass roomClass) {
        roomSetting.setRoomClassId(roomClass.getId());
        roomSetting.setBaseIsActive(roomClass.isActive());

        roomSetting.setBaseIsLocalBookingActive(roomClass.isLocalBookingActive());
        roomSetting.setBasePriceLocal(roomClass.getPriceLocal());
        roomSetting.setBasePriceLocalCurrency(roomClass.getPriceLocalCurrency());


        roomSetting.setBaseIsInternationalBookingActive(roomClass.isInternationalBookingActive());
        roomSetting.setBasePriceInternationalCurrency(roomClass.getPriceInternationalCurrency());
        roomSetting.setBasePriceInternational(roomClass.getPriceInternational());


    }

    private Optional<RoomClassConfig> getRoomClasConfig(RoomClass roomClass, LocalDate date) {
        Optional<List<RoomClassConfig>> roomClassConfigList = roomClassConfigRepository.findRoomsClassConfigByIdAndDateAndIsActive(roomClass.getId(), date);
        if (roomClassConfigList.isEmpty()) {
            return Optional.empty();
        } else if (roomClassConfigList.get().size() == 1) {
            return Optional.of(roomClassConfigList.get().getFirst());
        } else {
            //TODO: Corrections needed trigger
            return Optional.of(roomClassConfigList.get().stream()
                    .max(Comparator.comparing(RoomClassConfig::getUpdatedAt))
                    .orElseThrow(RoomClassConfigNotFoundException::new));
        }
    }

    /**
     * @param roomClassConfig: 401
     * @param roomSetting:     2015-01-28
     */
    private void validateRoomSettingWithRoomClassConfig(RoomSetting roomSetting, Optional<RoomClassConfig> roomClassConfig) throws RoomClassConfigNotFoundException {

        if (roomClassConfig.isEmpty()) {

            //If RoomClassConfig Not Found update the Base values (value from RoomClass as default values)
            roomSetting.setCalculatedIsActive(roomSetting.isBaseIsActive());

            roomSetting.setCalcPriceLocal(roomSetting.getBasePriceLocal());
            roomSetting.setCalcPriceLocalCurrency(roomSetting.getBasePriceLocalCurrency());
            roomSetting.setCalIsLocalBookingActive(roomSetting.isBaseIsLocalBookingActive());

            roomSetting.setCalcPriceInternational(roomSetting.getBasePriceInternational());
            roomSetting.setCalcPriceInternationalCurrency(roomSetting.getBasePriceInternationalCurrency());
            roomSetting.setCalcIsInternationalBookingActive(roomSetting.isBaseIsInternationalBookingActive());

        } else {
            roomSetting.setCalculatedIsActive(roomClassConfig.get().isActive());

            roomSetting.setCalcPriceLocal(roomClassConfig.get().getPriceLocal());
            roomSetting.setCalcPriceLocalCurrency(roomClassConfig.get().getPriceLocalCurrency());
            roomSetting.setCalIsLocalBookingActive(roomClassConfig.get().isLocalBookingActive());

            roomSetting.setCalcPriceInternational(roomClassConfig.get().getPriceInternational());
            roomSetting.setCalcPriceInternationalCurrency(roomClassConfig.get().getPriceInternationalCurrency());
            roomSetting.setCalcIsInternationalBookingActive(roomClassConfig.get().isInternationalBookingActive());
        }


    }





//
//    /**
//     * @param roomClassId: 401
//     * @param date:        2015-01-28
//     */
//    private RoomClassConfig getRoomClassConfigValidation(final int roomClassId, final LocalDate date) throws RoomClassConfigNotFoundException {
//        Optional<List<RoomClassConfig>> roomClassConfigList = roomClassConfigRepository.findRoomsClassConfigByIdAndDateAndIsActive(roomClassId, date);
//        if (roomClassConfigList.isEmpty()) {
//            throw new RoomClassConfigNotFoundException();
//        } else {
//            Optional<RoomConfig> roomConfig;
//            if (roomClassConfigList.get().size() > 1) {
//                //TODO: Corrections needed trigger
//
//                return roomClassConfigList.get().stream()
//                        .max(Comparator.comparing(RoomClassConfig::getUpdatedAt))
//                        .orElseThrow(RoomClassConfigNotFoundException::new);
//            } else {
//                return roomClassConfigList.get().getFirst();
//            }
//        }
//    }


    /**
     * @param date: date
     */
    public List<RoomSetting> getRoomSettingList(final LocalDate date) throws RoomNotFoundException {
        List<Room> roomList = roomRepository.findAllByIsActive(true);
        return roomList.stream()
                .filter(r ->
                        r.getRoomClass().isActive()
                )
                .map(r -> {
                            boolean localBookingActive =
                                    r.getRoomClass().isLocalBookingActive()
                                            && r.isLocalBookingActive();

                            boolean internationalBookingActive =
                                    r.getRoomClass().isInternationalBookingActive()
                                            && r.isInternationalBookingActive();

                            return new RoomSetting(

                                    r.getRoomClass().getId(),
                                    date,
                                    localBookingActive,
                                    internationalBookingActive,
                                    r.getRoomClass().getPriceLocal(),
                                    r.getRoomClass().getPriceLocalCurrency(),
                                    r.getRoomClass().getPriceInternational(),
                                    r.getRoomClass().getPriceInternationalCurrency()
                            );
                        }

                )
                .toList();

    }
}
