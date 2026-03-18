package com.beryllinus.hotel_service.contoller;

import com.beryllinus.hotel_service.dto.LoginRequestDTO;
import com.beryllinus.hotel_service.dto.LoginResponseDTO;
import com.beryllinus.hotel_service.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    LoginResponseDTO loginResponseDTO;

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        loginService.authenticate(loginRequestDTO);
        return loginResponseDTO;
    }
}
