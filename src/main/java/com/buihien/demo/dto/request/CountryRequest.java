package com.buihien.demo.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryRequest {
    @NotBlank(message = "name not blank")
    private String name;

    @NotBlank(message = "code not blank")
    private String code;

    @NotBlank(message = "description not blank")
    private String description;
}