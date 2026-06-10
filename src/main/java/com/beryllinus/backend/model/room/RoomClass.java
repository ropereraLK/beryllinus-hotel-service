package com.beryllinus.backend.model.room;

import com.beryllinus.backend.enumuration.Currency;
import com.beryllinus.backend.enumuration.RoomClassType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_class")
public class RoomClass  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(value = EnumType.STRING)
    private RoomClassType roomClassType;
    private String description;
    private boolean isActive;

    @Column(precision = 10, scale = 2)
    private BigDecimal priceLocal;
    @Enumerated(EnumType.STRING)
    private Currency priceLocalCurrency;
    private boolean isLocalBookingActive;

    @Column(precision = 10, scale = 2)
    private BigDecimal priceInternational;
    @Enumerated(EnumType.STRING)
    private Currency priceInternationalCurrency;
    private boolean isInternationalBookingActive;



    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomClass roomClass = (RoomClass) o;
        return roomClassType == roomClass.roomClassType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomClassType);
    }
}
