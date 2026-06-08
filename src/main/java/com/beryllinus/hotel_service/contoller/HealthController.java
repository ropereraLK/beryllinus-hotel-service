package com.beryllinus.hotel_service.contoller;

import com.beryllinus.hotel_service.dto.response.HealthDTO;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private ObjectProvider<HealthDTO> healthDTOProvider;


    @GetMapping
    public ResponseEntity<HealthDTO> getHealth(){
        //TODO: Update the version
        return ResponseEntity.ok(healthDTOProvider.getObject());
    }
}
