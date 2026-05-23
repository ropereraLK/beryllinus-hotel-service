package com.beryllinus.hotel_service.service;

import com.beryllinus.hotel_service.exceptions.RoomConfigNotFoundException;
import com.beryllinus.hotel_service.exceptions.RoomNotFoundException;
import com.beryllinus.hotel_service.model.room.Room;
import com.beryllinus.hotel_service.model.room.RoomConfig;
import com.beryllinus.hotel_service.repository.RoomConfigRepository;
import com.beryllinus.hotel_service.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OverlapDetectionService {
    private final RoomConfigRepository roomConfigRepository;

    @Autowired
    private RoomRepository roomRepository;

    public OverlapDetectionService(RoomConfigRepository roomConfigRepository) {
        this.roomConfigRepository = roomConfigRepository;
    }

    /**
     * @param roomId: 401
     * @param date:   2015-01-28
     */
    public RoomConfig getRoomConfig(int roomId, LocalDate date) throws RoomConfigNotFoundException {
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
     * @param roomId: 401
     * @param date:   2015-01-28
     */
    public RoomConfig getRoom(int roomId, LocalDate date) throws RoomNotFoundException {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isEmpty()){
            throw new RoomNotFoundException();
        }
    }
}
