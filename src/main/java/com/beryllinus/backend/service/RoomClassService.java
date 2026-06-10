package com.beryllinus.backend.service;


import com.beryllinus.backend.constants.CacheConstants;
import com.beryllinus.backend.exceptions.RoomClassNotFoundException;
import com.beryllinus.backend.model.room.RoomClass;
import com.beryllinus.backend.enumuration.RoomClassType;
import com.beryllinus.backend.repository.RoomClassRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Service
public class RoomClassService {

    private static final Logger LOGGER = LogManager.getLogger(RoomClassService.class);
    private final RoomClassRepository roomClassRepository;

    public RoomClassService(
            RoomClassRepository roomClassRepository
    ) {
        this.roomClassRepository = roomClassRepository;
    }

    /**
     * Get all active RoomClasses.
     */
    @Cacheable(CacheConstants.ROOM_CLASSES)
    public List<RoomClass> getAllActiveRoomClasses() {
        LOGGER.info("Fetching data from database");
        return roomClassRepository.findAllByIsActive(true);
    }

    /**
     * Get RoomClass by id.
     */
    public RoomClass getRoomClassById(
            Integer id
    ) {

        return getAllActiveRoomClasses()
                .stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(
                        () -> new RoomClassNotFoundException(
                                "RoomClass not found. id=" + id
                        )
                );
    }

    /**
     * Get RoomClass by business enum.
     */
    public RoomClass getRoomClassByType(
            RoomClassType roomClassType
    ) {
        return
                getAllActiveRoomClasses()
                        .stream()
                        .filter(r -> r.getRoomClassType().equals(roomClassType))
                        .findFirst()
                        .orElseThrow(
                                () -> new RoomClassNotFoundException(
                                        "RoomClass not found. type=" + roomClassType
                                )
                        );
    }
}