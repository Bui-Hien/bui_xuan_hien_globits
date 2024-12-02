package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.UserRequest;
import com.buihien.demo.dto.response.PersonResponse;
import com.buihien.demo.dto.response.RoleResponse;
import com.buihien.demo.dto.response.UserResponse;
import com.buihien.demo.entities.Company;
import com.buihien.demo.entities.Person;
import com.buihien.demo.entities.Role;
import com.buihien.demo.entities.User;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.UserRepository;
import com.buihien.demo.services.CompanyService;
import com.buihien.demo.services.PersonService;
import com.buihien.demo.services.RoleService;
import com.buihien.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PersonService personService;
    private final CompanyService companyService;
    private final RoleService roleService;

    @Override
    public long saveUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ResourceNotFoundException("Email already exists.");
        }
        if (personService.existsByPhoneNumber(userRequest.getPerson().getPhoneNumber())) {
            throw new ResourceNotFoundException("Phone number already exists.");
        }

        var company = companyService.getCompanyById(userRequest.getPerson().getCompanyId());
        User user = toUserEntity(userRequest);
        user.getPerson().setCompany(Company.builder()
                .id(company.getId())
                .build());
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        userRequest.getRoles().forEach(role -> {
            var ruleRequest = roleService.getRoleById(role.getId());
            user.getRoles().add(
                    Role.builder()
                            .id(ruleRequest.getId())
                            .build());
        });
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public long updateUser(long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + id));
        user.setPassword(userRequest.getPassword());
        user.setIsActive(userRequest.getIsActive());


        userRequest.getRoles().forEach(role -> {
            var ruleRequest = roleService.getRoleById(role.getId());
            user.getRoles().add(
                    Role.builder()
                            .id(ruleRequest.getId())
                            .build());
        });
        user.getPerson().setFullName(userRequest.getPerson().getFullName());
        user.getPerson().setGender(userRequest.getPerson().getGender());
        user.getPerson().setBirthdate(userRequest.getPerson().getBirthdate());
        user.getPerson().setAddress(userRequest.getPerson().getAddress());

        if (!personService.existsByPhoneNumber(userRequest.getPerson().getPhoneNumber())
                && !Objects.equals(userRequest.getPerson().getPhoneNumber(), user.getPerson().getPhoneNumber())
        ) {
            user.getPerson().setPhoneNumber(userRequest.getPerson().getPhoneNumber());
        }

        var company = companyService.getCompanyById(userRequest.getPerson().getCompanyId());

        user.getPerson().setCompany(Company.builder()
                .id(company.getId())
                .name(company.getName())
                .code(company.getCode())
                .address(company.getAddress())
                .build());

        userRepository.save(user);
        return user.getId();
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .isActive(user.getIsActive())
                        .person(PersonResponse.builder()
                                .id(user.getPerson().getId())
                                .fullName(user.getPerson().getFullName())
                                .gender(user.getPerson().getGender())
                                .birthdate(user.getPerson().getBirthdate())
                                .idCompany(user.getPerson().getCompany().getId())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + id));
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .isActive(user.getIsActive())
                .person(PersonResponse.builder()
                        .id(user.getPerson().getId())
                        .fullName(user.getPerson().getFullName())
                        .gender(user.getPerson().getGender())
                        .birthdate(user.getPerson().getBirthdate())
                        .idCompany(user.getPerson().getCompany().getId())
                        .build())
                .roles(user.getRoles()
                        .stream().map(role ->
                                RoleResponse.builder()
                                        .id(role.getId())
                                        .role(role.getRole())
                                        .description(role.getDescription())
                                        .build()).
                        collect(Collectors.toList()))
                .build();
    }

    @Override
    public void deleteUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + id));
        userRepository.delete(user);
    }

    private User toUserEntity(UserRequest userRequest) {
        return User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword()) //hash password
                .isActive(userRequest.getIsActive())
                .person(
                        Person.builder()
                                .fullName(userRequest.getPerson().getFullName())
                                .gender(userRequest.getPerson().getGender())
                                .birthdate(userRequest.getPerson().getBirthdate())
                                .phoneNumber(userRequest.getPerson().getPhoneNumber())
                                .address(userRequest.getPerson().getAddress())
                                .build()
                )
                .build();
    }
}
