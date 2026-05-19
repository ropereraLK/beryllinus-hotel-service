package com.beryllinus.hotel_service.scheduler;

import com.beryllinus.hotel_service.model.room.Room;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RoomAvailabilityScheduler {

    //Start schedule for the date 2027-05-19
    //if 02-28 -- check for leap years and add for 02-29 as well
    //if today is 02-29 skill scheuler doday

    //check the Room table whether any rooms available to schedule today
    // match whether room classes also allowed
    //if overlapping data found get only the latest value but trigger data correction needed trigger

    private Set<Room> rooms = Collections.synchronizedSet(new HashSet<>());


    //do all the validations and create tge roomBooking data in a loop
}
