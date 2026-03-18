package com.beryllinus.hotel_service.service;

import com.beryllinus.hotel_service.config.AuthProperties;
import com.beryllinus.hotel_service.dto.LoginRequestDTO;
import com.beryllinus.hotel_service.enumuration.AuthProvider;
import com.beryllinus.hotel_service.factory.AuthHandlerFactory;
import com.beryllinus.hotel_service.handler.AuthHandler;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private AuthHandlerFactory authHandlerFactory;
    private AuthProperties authProperties;

    public void authenticate(LoginRequestDTO loginRequestDTO) {

       AuthHandler handler = authHandlerFactory.getHandler(AuthProvider.valueOf(authProperties.getProvider().toUpperCase()));
       handler.login(loginRequestDTO);

    }
}
