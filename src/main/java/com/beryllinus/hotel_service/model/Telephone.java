package com.beryllinus.hotel_service.model;

import lombok.Data;

@Data
public class Telephone {

    private String telephoneNumber;
    private String countryCode;
    private boolean isWhatsappAvailable;
}
