package com.beryllinus.hotel_service.model;

import lombok.Data;



import java.time.Instant;

//@Document(collection = "users")
@Data
public class Guest {


        private String id;

        private String firstName;
        private String lastName;


        private String email;

        private GuestIdentification identification;
        private Telephone telephone;

        private Instant createdAt;
        private Instant updatedAt;

}
