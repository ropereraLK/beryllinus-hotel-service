package com.beryllinus.hotel_service.service;

import com.beryllinus.hotel_service.exceptions.RoomConfigNotFoundException;
import com.beryllinus.hotel_service.exceptions.RoomNotFoundException;
import com.beryllinus.hotel_service.model.room.Room;
import com.beryllinus.hotel_service.model.room.RoomConfig;
import com.beryllinus.hotel_service.repository.RoomConfigRepository;
import com.beryllinus.hotel_service.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomConfigRepository roomConfigRepository;

    public RoomService(RoomRepository roomRepository, RoomConfigRepository roomConfigRepository) {
        this.roomRepository = roomRepository;
        this.roomConfigRepository = roomConfigRepository;
    }

    public List<Room> getAllActiveRooms() throws RoomNotFoundException {
        List<Room> roomList = roomRepository.findAllByIsActive(true);

        if (roomList.isEmpty()) {
            throw new RoomNotFoundException();
        } else {
            return roomList;
        }
    }

    public Optional<RoomConfig> getRoomConfigByDate(Room room, LocalDate date) throws RoomConfigNotFoundException {
        List<RoomConfig> roomConfigList = roomConfigRepository.findRoomsConfigByRoomIdAndDateAndIsActive(room.getId(), date);
        if (roomConfigList.isEmpty()) {
            return Optional.empty();
        } else if (roomConfigList.size() > 1) {
            //TODO: Trigger Correction needed error
            return roomConfigList.stream().max(Comparator.comparing(RoomConfig::getUpdatedAt));
        } else {
            return Optional.of(roomConfigList.getFirst());
        }
    }

}
