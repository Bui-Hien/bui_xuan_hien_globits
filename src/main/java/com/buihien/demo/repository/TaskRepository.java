package com.buihien.demo.repository;

import com.buihien.demo.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
