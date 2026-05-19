package com.beryllinus.hotel_service.model.room;

import com.beryllinus.hotel_service.enumuration.RoomClassType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
//Represents the physically existing room
public class Room {
    private int id;
    private RoomClassType roomClassType;
    private String roomNumber;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Room room))
            return false;

        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
