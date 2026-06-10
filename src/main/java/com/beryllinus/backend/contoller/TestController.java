package com.beryllinus.backend.contoller;

import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable("test")
    @GetMapping("/cache")
    public String testCache() {
        return "hello";
    }
}