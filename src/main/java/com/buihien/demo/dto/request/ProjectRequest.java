package com.buihien.demo.dto.request;


import com.buihien.demo.dto.validator.ValidPersonIds;
import jakarta.validation.constraints.Min;
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
public class ProjectRequest implements Serializable {
    @NotBlank(message = "code not blank")
    private String code;

    @NotBlank(message = "name not blank")
    private String name;

    @NotBlank(message = "description not blank")
    private String description;

    @Min(value = 1, message = "companyId >1")
    private long companyId;

    @ValidPersonIds
    private Set<Long> personIds = new HashSet<>();
}
