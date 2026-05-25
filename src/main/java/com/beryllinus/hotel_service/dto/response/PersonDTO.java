package com.beryllinus.hotel_service.dto.response;

import lombok.Data;

@Data
public class PersonDTO {
    private String firstName;
    private String lastName;

    private String country;
    private String email;

    private String passportCountry;
    private String passportIdentificationNumber;

    private String telephoneTelephoneNumber;
    private String telephoneCountryCode;
    private boolean telephoneIsWhatsappAvailable;

    private String altTelephoneTelephoneNumber;
    private String altTelephoneCountryCode;
    private boolean altTelephoneIsWhatsappAvailable;

    private String nic;
    private String drivingLicence;
}
