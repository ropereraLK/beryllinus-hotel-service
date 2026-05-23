package com.beryllinus.hotel_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Document(collection = "bookings")
@Component
public class Booking {

    private String id;

    private String name;


}
