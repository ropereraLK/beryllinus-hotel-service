package com.beryllinus.hotel_service.contoller;

import com.beryllinus.hotel_service.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    public BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }



//    @GetMapping("/{bookingName}")
//    public ResponseEntity<String> testBooking(@PathVariable String bookingName) {
//        return ResponseEntity.ok(bookingService.createTestBooking(bookingName));
//    }
}
