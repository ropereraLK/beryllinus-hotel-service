package com.beryllinus.hotel_service.mapper;

import com.beryllinus.hotel_service.dto.Passport;
import com.beryllinus.hotel_service.dto.response.PersonDTO;
import com.beryllinus.hotel_service.model.Person;
import com.beryllinus.hotel_service.model.Telephone;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    public PersonDTO toPersonResponse(Person person) {

        PersonDTO response = new PersonDTO();

        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());

        response.setCountry(person.getCountry());
        response.setEmail(person.getEmail());

        if (person.getPassport() != null) {

            response.setPassportCountry(
                    person.getPassport().country()
            );

            response.setPassportIdentificationNumber(
                    person.getPassport().identificationNumber()
            );
        }

        if (person.getTelephone() != null) {

            response.setTelephoneTelephoneNumber(
                    person.getTelephone().telephoneNumber()
            );

            response.setTelephoneCountryCode(
                    person.getTelephone().countryCode()
            );

            response.setTelephoneIsWhatsappAvailable(
                    person.getTelephone().isWhatsappAvailable()
            );
        }

        if (person.getAltTelephone() != null) {

            response.setAltTelephoneTelephoneNumber(
                    person.getAltTelephone().telephoneNumber()
            );

            response.setAltTelephoneCountryCode(
                    person.getAltTelephone().countryCode()
            );

            response.setAltTelephoneIsWhatsappAvailable(
                    person.getAltTelephone().isWhatsappAvailable()
            );
        }

        response.setNic(person.getNic());
        response.setDrivingLicence(person.getDrivingLicence());

        return response;
    }

    public Person toPerson(PersonDTO dto) {

        if (dto == null) {
            return null;
        }

        Person person = new Person();

        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setCountry(dto.getCountry());
        person.setEmail(dto.getEmail());

        person.setPassport(
                new Passport(
                        dto.getPassportCountry(),
                        dto.getPassportIdentificationNumber()
                )
        );

        person.setTelephone(
                new Telephone(
                        dto.getTelephoneTelephoneNumber(),
                        dto.getTelephoneCountryCode(),
                        dto.isTelephoneIsWhatsappAvailable()
                )
        );

        person.setAltTelephone(
                new Telephone(
                        dto.getAltTelephoneTelephoneNumber(),
                        dto.getAltTelephoneCountryCode(),
                        dto.isAltTelephoneIsWhatsappAvailable()
                )
        );

        person.setNic(dto.getNic());
        person.setDrivingLicence(dto.getDrivingLicence());

        return person;
    }

    public PersonDTO toDTO(Person person) {

        if (person == null) {
            return null;
        }

        PersonDTO dto = new PersonDTO();

        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setCountry(person.getCountry());
        dto.setEmail(person.getEmail());

        if (person.getPassport() != null) {
            dto.setPassportCountry(person.getPassport().country());
            dto.setPassportIdentificationNumber(
                    person.getPassport().identificationNumber()
            );
        }

        if (person.getTelephone() != null) {
            dto.setTelephoneTelephoneNumber(
                    person.getTelephone().telephoneNumber()
            );
            dto.setTelephoneCountryCode(
                    person.getTelephone().countryCode()
            );
            dto.setTelephoneIsWhatsappAvailable(
                    person.getTelephone().isWhatsappAvailable()
            );
        }

        if (person.getAltTelephone() != null) {
            dto.setAltTelephoneTelephoneNumber(
                    person.getAltTelephone().telephoneNumber()
            );
            dto.setAltTelephoneCountryCode(
                    person.getAltTelephone().countryCode()
            );
            dto.setAltTelephoneIsWhatsappAvailable(
                    person.getAltTelephone().isWhatsappAvailable()
            );
        }

        dto.setNic(person.getNic());
        dto.setDrivingLicence(person.getDrivingLicence());

        return dto;
    }
}
