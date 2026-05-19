package com.beryllinus.hotel_service.model.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomConfig {

    private int id;
    private boolean isActive;
    private Date startDate;
    private Date endDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;

}
