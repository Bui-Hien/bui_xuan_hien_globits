package com.buihien.demo.controller;

import com.buihien.demo.dto.request.UserRequest;
import com.buihien.demo.dto.response.generic.ResponseData;
import com.buihien.demo.dto.response.generic.ResponseError;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    public ResponseData<?> addUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Request add country {}", userRequest);

        try {
            long id = userService.saveUser(userRequest);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User add success", id);
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.CONFLICT.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }
    }

    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) long userId, @Valid @RequestBody UserRequest userRequest) {
        log.info("Request update user Id={}", userId);

        try {
            userService.updateUser(userId, userRequest);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User update success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }
    }

    @PatchMapping("/{userId}")
    public ResponseData<?> updateUserAvatar(@PathVariable @Min(1) long userId, MultipartFile avatar) {
        log.info("Request update user Id={}", userId);

        try {
            userService.updateUserAvatar(userId, avatar);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User update success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable @Min(value = 1, message = "Id user must be greater than 0") long userId) {
        log.info("Request delete userId={}", userId);

        try {
            userService.deleteUserById(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User delete success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }
    }

    @GetMapping("/{userId}")
    public ResponseData<?> getCountry(@PathVariable @Min(value = 1, message = "Id user must be greater than 0") long userId) {
        log.info("Request get user by id={}", userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get user", userService.getUserById(userId));
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allUser")
    public ResponseData<?> getAllCountry() {
        log.info("Request get all user");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all user", userService.getAllUser());
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allUserWithPage")
    public ResponseData<?> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                       @Min(10) @RequestParam(defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "allUserWithPage", userService.getAllUsersWithPage(pageNo, pageSize));
    }
}
