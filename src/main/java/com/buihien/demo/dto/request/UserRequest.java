package com.buihien.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
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

    @NotNull(message = "Role ID cannot be null")
    @Min(value = 1, message = "Role ID must be greater than 0")
    private Set<Long> roles = new HashSet<>();

}
