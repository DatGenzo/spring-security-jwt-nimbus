package com.thanhdat.jwtnimbus.services.impl;

import com.thanhdat.jwtnimbus.dtos.*;
import com.thanhdat.jwtnimbus.entities.*;
import com.thanhdat.jwtnimbus.exceptions.DuplicateResourceException;
import com.thanhdat.jwtnimbus.repositories.UserRepository;
import com.thanhdat.jwtnimbus.services.*;
import java.util.Locale;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final NimbusJwtService jwtService;

    public AuthenticationServiceImpl(UserRepository repository, PasswordEncoder encoder,
        AuthenticationManager manager, NimbusJwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.manager = manager;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (repository.existsByEmailIgnoreCase(email))
            throw new DuplicateResourceException("Email đã được sử dụng");
        User user = new User(request.fullName().trim(), email, encoder.encode(request.password()),
            Role.USER, true, true);
        return UserResponse.from(repository.save(user));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Authentication auth = manager.authenticate(new UsernamePasswordAuthenticationToken(
            request.email().trim().toLowerCase(Locale.ROOT), request.password()));
        User user = (User) auth.getPrincipal();
        return new LoginResponse(jwtService.generateToken(user), "Bearer",
            jwtService.getExpirationTime(), UserResponse.from(user));
    }
}
