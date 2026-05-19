package com.beryllinus.hotel_service.repository;

import com.beryllinus.hotel_service.model.room.Room;
import com.beryllinus.hotel_service.model.room.RoomConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomConfigRepository extends JpaRepository<RoomConfig, Integer> {
    @Query("""
                SELECT r
                FROM RoomConfig r
                WHERE r.id = :roomId
                AND r.isActive = true
                AND :targetDate BETWEEN r.startDate AND r.endDate
            """)
    List<RoomConfig> findRoomsConfigByRoomIdAndDateAndIsActive(
            @Param("roomId") Integer roomId,
            @Param("targetDate") LocalDate targetDate
    );

}