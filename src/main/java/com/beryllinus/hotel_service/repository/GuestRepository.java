package com.beryllinus.hotel_service.repository;

import com.beryllinus.hotel_service.enumuration.IdentificationType;
import com.beryllinus.hotel_service.model.Guest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestRepository extends MongoRepository<Guest, UUID> {
    Optional<Guest> findByEmail(String email);
    List<Guest>  findByGuestIdentification_Number(String number);
    List<Guest> findByGuestIdentification_IdentificationTypeAndGuestIdentification_Number(
            IdentificationType type,
            String number
    );
    List<Guest> findByTelephone_TelephoneNumber(
            String telephone
    );

}
