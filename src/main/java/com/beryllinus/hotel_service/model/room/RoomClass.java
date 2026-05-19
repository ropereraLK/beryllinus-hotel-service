package com.beryllinus.hotel_service.model.room;

import com.beryllinus.hotel_service.enumuration.Currency;
import com.beryllinus.hotel_service.enumuration.RoomClassType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomClass {

    private int id;
    private RoomClassType roomClassType;
    private String description;
    private boolean isActive;
    private Date activeStartDate;
    private Date activeEndDate;
    private double priceLocal;
    private Currency priceLocalCurrency;
    private double priceInternational;
    private Currency priceInternationalCurrency;
    private boolean isLocalBookingActive;
    private boolean isInternationalBookingActive;
}
