package com.buihien.demo.repository;

import com.buihien.demo.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT t " +
            "FROM Task t " +
            "JOIN t.person p " +
            "JOIN t.project pr " +
            "JOIN p.company c " +
            "WHERE t.name LIKE CONCAT('%', :nameTask, '%') " +
            "AND p.fullName LIKE CONCAT('%', :namePerson, '%') " +
            "AND pr.name LIKE CONCAT('%', :nameProject, '%') " +
            "AND c.name LIKE CONCAT('%', :nameCompany, '%')")
    List<Task> findByNamedsfdsaf(@Param("nameTask") String nameTask,
                    @Param("namePerson") String namePerson,
                    @Param("nameProject") String nameProject,
                    @Param("nameCompany") String nameCompany);
}