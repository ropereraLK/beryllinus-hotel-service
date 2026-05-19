package com.beryllinus.hotel_service.service;

import com.beryllinus.hotel_service.model.room.RoomConfig;
import com.beryllinus.hotel_service.repository.RoomConfigRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OverlapDetectionService {
    private final RoomConfigRepository roomConfigRepository;

    public OverlapDetectionService(RoomConfigRepository roomConfigRepository) {
        this.roomConfigRepository = roomConfigRepository;
    }

    /**
     * @param roomId: 401
     * @param date:   2015-01-28
     */
    public void detectOverlappedDates(int roomId, LocalDate date) {

        List<RoomConfig> roomConfigList = roomConfigRepository.findRoomsConfigByRoomIdAndDateAndIsActive(roomId, date);
        //sort the arraylist my updatedAt
        Optional<RoomConfig> roomConfig = roomConfigList.stream()
                .max(Comparator.comparing(RoomConfig::getUpdatedAt));

        if (roomConfig.isPresent()){
            //Check for class activity

        }else throw new RuntimeException();

    }
}
