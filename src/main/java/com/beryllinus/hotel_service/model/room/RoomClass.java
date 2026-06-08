package com.beryllinus.hotel_service.model.room;

import com.beryllinus.hotel_service.enumuration.Currency;
import com.beryllinus.hotel_service.enumuration.RoomClassType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_class")
public class RoomClass {

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

    /**
     * This is a calculated field based on total number of Rooms available
     * in the Room Class
     * Eg: If 10 Rooms available for RoomClass 'STANDARD'
     * This field is 10 and this will never be changed
     * Always try to refer roomClassConfig availableRooms value
     */
    private int availableRooms;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
