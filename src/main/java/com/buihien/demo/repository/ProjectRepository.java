package com.buihien.demo.repository;

import com.buihien.demo.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByCode(String code);

}
