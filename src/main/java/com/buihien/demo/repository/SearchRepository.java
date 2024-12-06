package com.buihien.demo.repository;


import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Company;
import com.buihien.demo.entities.Person;
import com.buihien.demo.entities.Project;
import com.buihien.demo.entities.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;



    public PageResponse<?> searchTasks(String nameTask, String nameProject, String nameCompany, String namePerson) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> task = cq.from(Task.class);

        Join<Task, Person> persionJoin = task.join("person", JoinType.INNER);
        Join<Person, Company> companyJoin = persionJoin.join("company", JoinType.INNER);
        Join<Task, Project> projectJoin = task.join("project", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        if (nameProject != null) {
            predicates.add(cb.like(projectJoin.get("name"), "%" + nameProject + "%"));
        }
        if (nameCompany != null) {
            predicates.add(cb.like(companyJoin.get("name"), "%" + nameCompany + "%"));
        }
        if (namePerson != null) {
            predicates.add(cb.like(persionJoin.get("fullName"), "%" + namePerson + "%"));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.or(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Task> query = entityManager.createQuery(cq);
        return PageResponse.builder()
                .items(query.getResultList())
                .build();
    }
}