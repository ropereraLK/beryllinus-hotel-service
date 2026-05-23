package com.beryllinus.hotel_service.repository;

import com.beryllinus.hotel_service.model.room.RoomConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomConfigRepository extends JpaRepository<RoomConfig, Long> {
    @Query("""
                SELECT r
                FROM RoomConfig r
                WHERE r.room.id = :roomId
                AND r.isActive = true
                AND :targetDate BETWEEN r.startDate AND r.endDate
            """)
    List<RoomConfig> findRoomsConfigByRoomIdAndDateAndIsActive(
            @Param("roomId") Integer roomId,
            @Param("targetDate") LocalDate targetDate
    );

}