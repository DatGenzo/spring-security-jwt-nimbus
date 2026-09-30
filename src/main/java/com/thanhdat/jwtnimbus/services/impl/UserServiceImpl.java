package com.thanhdat.jwtnimbus.services.impl;

import com.thanhdat.jwtnimbus.dtos.UserResponse;
import com.thanhdat.jwtnimbus.entities.User;
import com.thanhdat.jwtnimbus.repositories.UserRepository;
import com.thanhdat.jwtnimbus.services.UserService;
import java.util.List;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(
        UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public User findEntityByEmail(String email) {
        return userRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(
                () -> new UsernameNotFoundException(
                    "Không tìm thấy người dùng có email: " + email
                )
            );
    }

    @Override
    public UserResponse findByEmail(String email) {
        return UserResponse.from(findEntityByEmail(email));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository
            .findAll()
            .stream()
            .map(UserResponse::from)
            .toList();
    }
}
