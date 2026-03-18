package com.beryllinus.hotel_service.handler;

import com.beryllinus.hotel_service.dto.LoginRequestDTO;
import com.beryllinus.hotel_service.dto.LoginResponseDTO;
import com.beryllinus.hotel_service.enumuration.AuthProvider;

public interface AuthHandler {
    AuthProvider getProvider();
    LoginResponseDTO login(LoginRequestDTO request);
}