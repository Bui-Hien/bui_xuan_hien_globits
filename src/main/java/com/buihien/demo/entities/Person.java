package com.buihien.demo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "address")
    private String address;
    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(mappedBy = "persons")
    private Set<Project> projects = new HashSet<>();

}
