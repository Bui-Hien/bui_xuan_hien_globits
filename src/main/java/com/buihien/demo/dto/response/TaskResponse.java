package com.buihien.demo.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;

    private LocalDate startTime;

    private LocalDate endTime;

    private String priority;

    private String name;

    private String description;

    private String status;

    private ProjectResponse project;

    private PersonResponse person;

    private CompanyResponse company;
}
