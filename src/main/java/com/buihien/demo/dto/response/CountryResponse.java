package com.buihien.demo.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {
    private Long id;

    private String name;

    private String code;

    private String description;
}
