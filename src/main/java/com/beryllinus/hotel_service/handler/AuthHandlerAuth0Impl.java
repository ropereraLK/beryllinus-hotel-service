package com.beryllinus.hotel_service.handler;

import com.beryllinus.hotel_service.dto.LoginRequestDTO;
import com.beryllinus.hotel_service.dto.LoginResponseDTO;
import com.beryllinus.hotel_service.enumuration.AuthProvider;
import org.springframework.stereotype.Component;

@Component//("AUTH0")
public class AuthHandlerAuth0Impl implements AuthHandler{
    @Override
    public AuthProvider getProvider() {
        return AuthProvider.AUTH0;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        //Set email, password
        //Get Credentials

        return null;
    }
}
