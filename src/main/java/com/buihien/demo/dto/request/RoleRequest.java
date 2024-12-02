package com.buihien.demo.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest implements Serializable {
    @NotBlank(message = "role not blank")
    private String role;
    @NotBlank(message = "description role not blank")
    private String description;
}
