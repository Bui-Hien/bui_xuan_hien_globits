package com.buihien.demo.entities;

import com.buihien.demo.util.Priority;
import com.buihien.demo.util.Status;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonManagedReference
    private Project project;

    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonManagedReference
    private Person person;
}
