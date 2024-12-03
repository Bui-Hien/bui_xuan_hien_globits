package com.buihien.demo.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;
    private String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Person> persons;

    @OneToMany(mappedBy = "company")
    private Set<Department> departments = new HashSet<>();
}
