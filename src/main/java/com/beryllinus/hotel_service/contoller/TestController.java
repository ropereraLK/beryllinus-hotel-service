package com.beryllinus.hotel_service.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    @Deprecated
    public ResponseEntity<String> test() {

        return ResponseEntity.ok("Deprecated Test endpoint");
    }
}