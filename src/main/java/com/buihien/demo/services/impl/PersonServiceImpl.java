package com.buihien.demo.services.impl;

import com.buihien.demo.entities.Person;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.PersonRepository;
import com.buihien.demo.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    @Override
    public Person getPersonById(long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return personRepository.existsByPhoneNumber(phoneNumber);
    }
}
