package com.buihien.demo.services;

import com.buihien.demo.dto.request.UserRequest;
import com.buihien.demo.dto.response.UserResponse;
import com.buihien.demo.dto.response.generic.PageResponse;
import jakarta.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    long saveUser(UserRequest userRequest);

    void updateUser(long id, UserRequest userRequest);

    List<UserResponse> getAllUser();

    UserResponse getUserById(long id);


    void deleteUserById(long id);

    void updateUserAvatar(long userId, MultipartFile avatar);

    PageResponse getAllUsersWithPage(int pageNo, @Min(10) int pageSize);
}
