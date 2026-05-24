package com.beryllinus.hotel_service.model.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor

/***
 * @ ROOM
 * id: 1
 * RoomClassType: EXECUTIVE
 * roomNumber: 401
 */
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_class_id")
    private RoomClass roomClass;

    private String roomNumber;

    private boolean isActive;
    private boolean isLocalBookingActive;
    private boolean isInternationalBookingActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

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
