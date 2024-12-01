package com.buihien.demo.dto.request;

import com.buihien.demo.entities.Person;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

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
    private PersonRequest person;

}
