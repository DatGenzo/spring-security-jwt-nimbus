package com.thanhdat.jwtnimbus.controllers;

import com.thanhdat.jwtnimbus.dtos.*;
import com.thanhdat.jwtnimbus.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService service;
    public AuthenticationController(AuthenticationService service) { this.service = service; }
    @PostMapping("/signup")
    ResponseEntity<UserResponse> signup(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }
    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request) { return service.login(request); }
}
