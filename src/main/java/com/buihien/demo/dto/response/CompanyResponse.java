package com.buihien.demo.dto.response;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private Long id;
    private String name;
    private String code;
    private String address;
    private Set<PersonResponse> persons = new HashSet<>();
    private Set<DepartmentResponse> departments = new HashSet<>();
}
