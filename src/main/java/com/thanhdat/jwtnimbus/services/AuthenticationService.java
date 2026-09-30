package com.thanhdat.jwtnimbus.services;

import com.thanhdat.jwtnimbus.dtos.*;

public interface AuthenticationService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
