package com.beryllinus.hotel_service.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GuestDrivingLicenceIdentification extends GuestIdentification {

    private LocalDate expDate;
}
