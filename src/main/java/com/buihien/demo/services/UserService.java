package com.buihien.demo.services;

import com.buihien.demo.dto.request.UserRequest;
import com.buihien.demo.dto.response.UserResponse;
import com.buihien.demo.entities.User;

import java.util.List;

public interface UserService {
    long saveUser(UserRequest userRequest);

    void updateUser(long id, UserRequest userRequest);

    List<UserResponse> getAllUser();

    UserResponse getUserById(long id);

    User getUserByIdE(long id);

    void deleteUserById(long id);
}
