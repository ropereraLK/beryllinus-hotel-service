package com.beryllinus.hotel_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
@Data
public class Guest {

        @Id
        private String id;

        private String firstName;
        private String lastName;

        @Indexed(unique = true)
        private String email;

        private GuestIdentification identification;
        private Telephone telephone;

        private Instant createdAt;
        private Instant updatedAt;

}
