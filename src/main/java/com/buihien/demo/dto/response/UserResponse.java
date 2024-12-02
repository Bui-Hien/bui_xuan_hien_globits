package com.buihien.demo.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String password;
    private boolean isActive;
    private PersonResponse person;
    private List<RoleResponse> roles;
}
