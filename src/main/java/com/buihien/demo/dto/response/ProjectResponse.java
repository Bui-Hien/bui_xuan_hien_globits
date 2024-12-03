package com.buihien.demo.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private CompanyResponse company;
    private Set<PersonResponse> persons;
}
