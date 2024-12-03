package com.buihien.demo.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest implements Serializable {
    @NotBlank(message = "code not blank")
    private String name;

    @NotBlank(message = "code not blank")
    private String code;

    @NotBlank(message = "code not blank")
    private String address;

    @Valid
    private Set<DepartmentRequest> department = new HashSet<>();
}
