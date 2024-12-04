package com.buihien.demo.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
    private Long id;
    private String fullName;
    private String gender;
    private Data birthdate;
    private Long idCompany;
}
