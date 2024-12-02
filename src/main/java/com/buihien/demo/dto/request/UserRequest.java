package com.buihien.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest implements Serializable {
    @Email(message = "Email is invalid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "isActive cannot be null")
    private Boolean isActive;

    @NotNull(message = "Person cannot be null")
    @Valid
    private PersonRequest person;

    @NotEmpty(message = "Role list cannot be empty")
    @Valid
    private Set<RoleIdRequest> roles;

}
