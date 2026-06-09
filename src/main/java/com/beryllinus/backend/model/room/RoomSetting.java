package com.beryllinus.backend.model.room;

import com.beryllinus.backend.enumuration.Currency;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"room_class_id", "date"})
        }
)
public class RoomSetting {

    /**
     * id means the unique key  RoomSettingId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Per RoomClass, Per Day one Setting file will be generated
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_class_id")
    private RoomClass roomClass;

    private LocalDate date;
    private boolean calculatedIsActive;

    private BigDecimal calcPriceLocal;
    private Currency calcPriceLocalCurrency;
    private boolean calIsLocalBookingActive;

    private BigDecimal calcPriceInternational;
    private Currency calcPriceInternationalCurrency;
    private boolean calcIsInternationalBookingActive;

    /**
     * Base Methods reflects values taken from RoomClass
     * Then if RoomClassConfig has necessary records based on that above fields
     * are updated
     * NOT SAVED IN DB
     */
    @Transient
    private boolean baseIsActive;
    @Transient
    private boolean baseIsLocalBookingActive;
    @Transient
    private boolean baseIsInternationalBookingActive;

    @Transient
    private BigDecimal basePriceLocal;
    @Transient
    private Currency basePriceLocalCurrency;

    @Transient
    private BigDecimal basePriceInternational;
    @Transient
    private Currency basePriceInternationalCurrency;

    /**
     * This is a calculated field based on total number of Rooms available
     * in the Room Class
     * Eg: If 10 Rooms available for RoomClass 'STANDARD'
     * This field is 10 and this will never be changed
     * Always try to refer roomClassConfig availableRooms value
     */
    private int availableRooms;

    public RoomSetting(RoomClass roomClass, LocalDate date) {
        this.roomClass = roomClass;
        this.date = date;
    }

}
