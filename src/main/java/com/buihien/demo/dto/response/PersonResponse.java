package com.buihien.demo.dto.response;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
    private Long id;
    private String fullName;
    private String gender;
    private Date birthdate;
    private Long idCompany;
}
