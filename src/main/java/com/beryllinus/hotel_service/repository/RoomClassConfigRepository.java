package com.beryllinus.hotel_service.repository;

import com.beryllinus.hotel_service.model.room.RoomClassConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomClassConfigRepository extends JpaRepository<RoomClassConfig, Integer> {
}
