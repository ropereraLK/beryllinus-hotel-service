package com.beryllinus.backend.service;

import com.beryllinus.backend.dto.request.BookingPrecheckRequest;
import com.beryllinus.backend.model.room.RoomClass;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private final RoomClassService roomClassService;
    private final RoomSettingService roomSettingService;

    public BookingService(
            RoomClassService roomClassService,
            RoomSettingService roomSettingService) {
        this.roomClassService = roomClassService;
        this.roomSettingService = roomSettingService;
    }

    public void getPreCheckBooking(BookingPrecheckRequest bookingPrecheckRequest) {

        List<RoomClass> roomClassList = new ArrayList<>();
        if (bookingPrecheckRequest.getRoomClassType() == null) {
            roomClassList.addAll(roomClassService.getAllActiveRoomClasses());
        } else {
            roomClassList.add(roomClassService.getRoomClassByType(bookingPrecheckRequest.getRoomClassType()));
        }

        for (RoomClass roomClass : roomClassList) {

            LocalDate date = bookingPrecheckRequest.getCheckIn();

            while (date.isBefore(bookingPrecheckRequest.getCheckOut())) {

//                RoomSetting roomSetting = roomSettingsService
//                        .getRoomSetting(roomClass, date);

                // availability checks

                date = date.plusDays(1);
            }
        }


    }
}
