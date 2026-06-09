package com.beryllinus.backend.repository;

import com.beryllinus.backend.model.room.RoomClassConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomClassConfigRepository extends JpaRepository<RoomClassConfig, Integer> {

    @Query("""
                SELECT r
                FROM RoomClassConfig r
                WHERE r.roomClass.id = :roomClassId
                  AND r.isActive = true
                  AND :targetDate BETWEEN r.startDate AND r.endDate
            """)
    Optional<List<RoomClassConfig>> findRoomsClassConfigByIdAndDateAndIsActive(
            @Param("roomClassId") Integer roomClassId,
            @Param("targetDate") LocalDate targetDate
    );
}
