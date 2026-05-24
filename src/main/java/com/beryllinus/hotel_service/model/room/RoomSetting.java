package com.beryllinus.hotel_service.model.room;

import com.beryllinus.hotel_service.enumuration.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RoomSetting {

    private int roomId;
    private int roomClassId;
    private LocalDate date;
    private boolean calculatedIsActive;

    private BigDecimal calcPriceLocal;
    private Currency calcPriceLocalCurrency;
    private boolean calIsLocalBookingActive;

    private BigDecimal calcPriceInternational;
    private Currency calcPriceInternationalCurrency;
    private boolean calcIsInternationalBookingActive;

    private boolean baseIsActive;
    private boolean baseIsLocalBookingActive;
    private boolean baseIsInternationalBookingActive;

    private BigDecimal basePriceLocal;
    private Currency basePriceLocalCurrency;

    private BigDecimal basePriceInternational;
    private Currency basePriceInternationalCurrency;

    public RoomSetting(int roomId,
                       int roomClassId,
                       LocalDate date,
                       boolean baseIsLocalBookingActive,
                       boolean baseIsInternationalBookingActive,
                       BigDecimal basePriceLocal,
                       Currency basePriceLocalCurrency,
                       BigDecimal basePriceInternational,
                       Currency basePriceInternationalCurrency

    ) {
        this.roomId = roomId;
        this.roomClassId = roomClassId;
        this.date = date;
        //Only true records are selected
        this.baseIsActive = true;
        this.baseIsLocalBookingActive = baseIsLocalBookingActive;
        this.baseIsInternationalBookingActive = baseIsInternationalBookingActive;
        this.basePriceLocal = basePriceLocal;
        this.basePriceLocalCurrency = basePriceLocalCurrency;
        this.basePriceInternational = basePriceInternational;
        this.basePriceInternationalCurrency = basePriceInternationalCurrency;


    }

//    static class RoomSettingBuilder(){
//
//    }
//

}
