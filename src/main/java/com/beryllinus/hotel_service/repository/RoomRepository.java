package com.beryllinus.hotel_service.repository;

import com.beryllinus.hotel_service.model.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

//    @Query("""
//                SELECT r
//                FROM Room r
//                WHERE r.id = :roomId
//                AND :targetDate BETWEEN r.startDate AND r.endDate
//            """)
//    Optional<List<Room>> findRoomsByRoomIdAndDate(
//            @Param("roomId") Integer roomId,
//            @Param("targetDate") LocalDate targetDate
//    );
}
