package com.buihien.demo.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleIdRequest implements Serializable {
    @NotNull(message = "Role ID cannot be null")
    @Min(value = 1, message = "Role ID must be greater than 0")
    private Long id;
}
