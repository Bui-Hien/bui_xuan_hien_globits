package com.buihien.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest implements Serializable {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    @NotNull(message = "Birthdate cannot be null")
    private LocalDate birthdate;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotNull(message = "Company id cannot be null")
    private long companyId;
}
