package com.buihien.demo.repository;

import com.buihien.demo.entities.Company;
import com.buihien.demo.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsById(long id);
}
