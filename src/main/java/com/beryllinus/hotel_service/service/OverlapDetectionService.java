package com.beryllinus.hotel_service.service;

import com.beryllinus.hotel_service.exceptions.RoomClassConfigNotFoundException;
import com.beryllinus.hotel_service.exceptions.RoomConfigNotFoundException;
import com.beryllinus.hotel_service.exceptions.RoomNotFoundException;
import com.beryllinus.hotel_service.model.room.*;
import com.beryllinus.hotel_service.repository.RoomClassConfigRepository;
import com.beryllinus.hotel_service.repository.RoomClassRepository;
import com.beryllinus.hotel_service.repository.RoomConfigRepository;
import com.beryllinus.hotel_service.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OverlapDetectionService {
    private final RoomConfigRepository roomConfigRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomClassRepository roomClassRepository;

    @Autowired
    private RoomClassConfigRepository roomClassConfigRepository;

    public OverlapDetectionService(RoomConfigRepository roomConfigRepository) {
        this.roomConfigRepository = roomConfigRepository;
    }

    /**
     * @param roomId: 401
     * @param date:   2015-01-28
     */
    private RoomConfig getRoomConfigValidation(final int roomId, final LocalDate date) throws RoomConfigNotFoundException {
        List<RoomConfig> roomConfigList = roomConfigRepository.findRoomsConfigByRoomIdAndDateAndIsActive(roomId, date);
        if (roomConfigList.isEmpty()) {
            throw new RoomConfigNotFoundException();
        } else {
            Optional<RoomConfig> roomConfig;
            if (roomConfigList.size() > 1) {
                //TODO: Corrections needed trigger

                return roomConfigList.stream()
                        .max(Comparator.comparing(RoomConfig::getUpdatedAt))
                        .orElseThrow(RoomConfigNotFoundException::new);
            } else {
                return roomConfigList.getFirst();
            }
        }
    }

    /**
     * @param roomClassId: 401
     * @param date:        2015-01-28
     */
    private RoomClassConfig getRoomClassConfigValidation(final int roomClassId, final LocalDate date) throws RoomClassConfigNotFoundException {
        List<RoomClassConfig> roomClassConfigList = roomClassConfigRepository.findRoomsClassConfigByIdAndDateAndIsActive(roomClassId, date);
        if (roomClassConfigList.isEmpty()) {
            throw new RoomClassConfigNotFoundException();
        } else {
            Optional<RoomConfig> roomConfig;
            if (roomClassConfigList.size() > 1) {
                //TODO: Corrections needed trigger

                return roomClassConfigList.stream()
                        .max(Comparator.comparing(RoomClassConfig::getUpdatedAt))
                        .orElseThrow(RoomClassConfigNotFoundException::new);
            } else {
                return roomClassConfigList.getFirst();
            }
        }
    }

    /**
     * @param roomSetting: roomSetting
     */
    public RoomSetting validateConfig(RoomSetting roomSetting) throws RoomNotFoundException {
        //get relevantRoomConfig
        RoomConfig roomConfig;
        RoomClassConfig roomClassConfig;
        try {
            roomConfig = getRoomConfigValidation(roomSetting.getRoomId(), roomSetting.getDate());
        } catch (RoomConfigNotFoundException e) {
            //No Room Config Found, Use Room Base Data
            //TODO: Log this
        }

        try {
            roomClassConfig = getRoomClassConfigValidation(roomSetting.getRoomClassId(), roomSetting.getDate());
        } catch (RoomClassConfigNotFoundException e) {
            //No Room Config Found, Use Room Base Data
            //TODO: Log this
        }

        return null;
    }

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
                                    r.getId(),
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
