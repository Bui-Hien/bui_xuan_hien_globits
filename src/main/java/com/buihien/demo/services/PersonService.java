package com.buihien.demo.services;


import com.buihien.demo.entities.Person;


public interface PersonService {


    Person getPersonById(long id);
    boolean existsByPhoneNumber(String phoneNumber);

}
