package com.thanhdat.jwtnimbus.services;

import com.thanhdat.jwtnimbus.dtos.UserResponse;
import com.thanhdat.jwtnimbus.entities.User;
import java.util.List;

public interface UserService {

    User findEntityByEmail(String email);

    UserResponse findByEmail(String email);

    List<UserResponse> findAll();
}
