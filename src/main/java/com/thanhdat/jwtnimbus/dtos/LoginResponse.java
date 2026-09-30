package com.thanhdat.jwtnimbus.dtos;

public record LoginResponse(
    String token,
    String tokenType,
    long expiresIn,
    UserResponse user
) {
}
