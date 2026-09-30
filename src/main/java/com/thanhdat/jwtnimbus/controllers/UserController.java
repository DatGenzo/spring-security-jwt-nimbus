package com.thanhdat.jwtnimbus.controllers;

import com.thanhdat.jwtnimbus.dtos.UserResponse;
import com.thanhdat.jwtnimbus.entities.User;
import com.thanhdat.jwtnimbus.services.UserService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }
    @GetMapping("/me") UserResponse me(@AuthenticationPrincipal User user) { return UserResponse.from(user); }
    @GetMapping List<UserResponse> all() { return service.findAll(); }
}
