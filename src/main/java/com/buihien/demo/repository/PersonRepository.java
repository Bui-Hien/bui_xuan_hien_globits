package com.buihien.demo.repository;

import com.buihien.demo.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
