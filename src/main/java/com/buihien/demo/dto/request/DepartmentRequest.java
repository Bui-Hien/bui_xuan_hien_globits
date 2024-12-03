package com.buihien.demo.dto.request;

import com.buihien.demo.entities.Department;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {
    @NotBlank(message = "code not blank")
    private String code;

    @NotBlank(message = "code not blank")
    private String name;

    private Department parent;
}
