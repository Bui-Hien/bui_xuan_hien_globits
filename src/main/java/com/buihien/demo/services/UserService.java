package com.buihien.demo.services;

import com.buihien.demo.dto.request.UserRequest;
import com.buihien.demo.entities.User;

import java.util.List;

public interface UserService {
    long saveUser(UserRequest userRequest);

    long updateUser(long id, UserRequest userRequest);

    List<User> getAllUser();

    User getUserById(long id);

    void deleteUserById(long id);
}
