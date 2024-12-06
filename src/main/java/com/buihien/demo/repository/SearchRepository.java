package com.buihien.demo.repository;


import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Company;
import com.buihien.demo.entities.Person;
import com.buihien.demo.entities.Project;
import com.buihien.demo.entities.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> advanceSearch(int pageNo, int pageSize, String taskName, String personName, String companyName, String projectName, String sortBy) {

        PageImpl<Task> tasks = getTasks(pageNo, pageSize, taskName, personName, companyName, projectName, sortBy);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(tasks.getTotalPages())
                .items(tasks.getContent())
                .build();
    }


    private PageImpl<Task> getTasks(int pageNo, int pageSize, String taskName, String personName, String companyName, String projectName, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = criteriaBuilder.createQuery(Task.class);
        Root<Task> taskRoot = query.from(Task.class);
        Join<Task, Person> personJoin = taskRoot.join("person");
        Join<Task, Project> projectJoin = taskRoot.join("project");
        Join<Person, Company> companyJoin = personJoin.join("company");

        Predicate predicate = criteriaBuilder.conjunction();
        if (taskName != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(taskName);
            if (matcher.find()) {
                Predicate personPredicate = criteriaBuilder.like(
                        taskRoot.get(matcher.group(1)), // e.g., field name
                        "%" + matcher.group(3) + "%"   // e.g., value for the field
                );
                predicate = criteriaBuilder.and(predicate, personPredicate);
            }
        }
        if (personName != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(personName);
            if (matcher.find()) {
                Predicate personPredicate = criteriaBuilder.like(
                        personJoin.get(matcher.group(1)), // e.g., field name
                        "%" + matcher.group(3) + "%"   // e.g., value for the field
                );
                predicate = criteriaBuilder.and(predicate, personPredicate);
            }
        }
        if (projectName != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(projectName);
            if (matcher.find()) {
                Predicate personPredicate = criteriaBuilder.like(
                        projectJoin.get(matcher.group(1)), // e.g., field name
                        "%" + matcher.group(3) + "%"   // e.g., value for the field
                );
                predicate = criteriaBuilder.and(predicate, personPredicate);
            }
        }
        if (companyName != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(companyName);
            if (matcher.find()) {
                Predicate personPredicate = criteriaBuilder.like(
                        companyJoin.get(matcher.group(1)), // e.g., field name
                        "%" + matcher.group(3) + "%"   // e.g., value for the field
                );
                predicate = criteriaBuilder.and(predicate, personPredicate);
            }
        }
        query.where(predicate);


        if (sortBy != null) {
            Pattern pattern = Pattern.compile("(\\w+?):(\\w+?):(asc|desc)"); // field:Entity:value
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                // Extract field name, entity, and sorting order
                String fieldName = matcher.group(1);  // e.g., field name
                String entity = matcher.group(2);     // e.g., Entity name
                String sortOrder = matcher.group(3);  // e.g., asc or desc
                // Create dynamic sorting predicate
                Path<Object> sortPath;
                if ("task".equalsIgnoreCase(entity)) {
                    sortPath = taskRoot.get(fieldName);  // Access Task entity field dynamically
                } else if ("person".equalsIgnoreCase(entity)) {
                    sortPath = personJoin.get(fieldName);  // Access Person entity field dynamically
                } else if ("project".equalsIgnoreCase(entity)) {
                    sortPath = projectJoin.get(fieldName);  // Access Project entity field dynamically
                } else if ("company".equalsIgnoreCase(entity)) {
                    sortPath = companyJoin.get(fieldName);  // Access Company entity field dynamically
                } else {
                    throw new IllegalArgumentException("Unknown entity: " + entity);
                }

                // Apply the sorting order based on the direction
                if ("asc".equalsIgnoreCase(sortOrder)) {
                    query.orderBy(criteriaBuilder.asc(sortPath));
                } else {
                    query.orderBy(criteriaBuilder.desc(sortPath));
                }
            }
        }
        // Get total count
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);

        Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        List<Task> tasks = entityManager.createQuery(query)
                .setFirstResult(pageNo * pageSize)  // Paging should consider pageNo and pageSize properly
                .setMaxResults(pageSize)
                .getResultList();

        return new PageImpl<>(tasks, PageRequest.of(pageNo, pageSize), totalElements);
    }

}