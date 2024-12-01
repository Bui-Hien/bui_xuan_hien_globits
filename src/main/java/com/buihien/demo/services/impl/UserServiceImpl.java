package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.CountryRequest;
import com.buihien.demo.dto.request.UserRequest;
import com.buihien.demo.dto.response.CountryResponse;
import com.buihien.demo.entities.Country;
import com.buihien.demo.entities.Person;
import com.buihien.demo.entities.User;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.CountryRepository;
import com.buihien.demo.repository.PersonRepository;
import com.buihien.demo.repository.UserRepository;
import com.buihien.demo.services.CountryService;
import com.buihien.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    @Override
    public long saveUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ResourceNotFoundException("Email already exists.");
        }
        if (personRepository.existsByPhoneNumber(userRequest.getPerson().getPhoneNumber())) {
            throw new ResourceNotFoundException("Phone number already exists.");
        }
        User user = toUserEntity(userRequest);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public long updateUser(long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + id));
        user.setPassword(userRequest.getPassword());
        user.setIsActive(userRequest.getIsActive());
        Person person = user.getPerson();
        if (person == null) {
            person = new Person();
        }
        person.setFullName(userRequest.getPerson().getFullName());
        person.setGender(userRequest.getPerson().getGender());
        person.setBirthdate(userRequest.getPerson().getBirthdate());
        person.setAddress(userRequest.getPerson().getAddress());

        user.setPerson(person);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .peek(user -> {
                    Person person = Person.builder()
                            .id(user.getPerson().getId())
                            .fullName(user.getPerson().getFullName())
                            .gender(user.getPerson().getGender())
                            .birthdate(user.getPerson().getBirthdate())
                            .phoneNumber(user.getPerson().getPhoneNumber())
                            .address(user.getPerson().getAddress())
                            .build();

                    user.setPerson(person);
                }).collect(Collectors.toList());
    }

    @Override
    public User getUserById(long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + id));
        Person person = user.getPerson();
        if (person != null) {
            user.setPerson(Person.builder()
                    .id(user.getPerson().getId())
                    .fullName(user.getPerson().getFullName())
                    .gender(user.getPerson().getGender())
                    .birthdate(user.getPerson().getBirthdate())
                    .phoneNumber(user.getPerson().getPhoneNumber())
                    .address(user.getPerson().getAddress())
                    .build());
        }
        return user;
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
