package com.buihien.demo.services;

import com.buihien.demo.dto.request.RoleRequest;
import com.buihien.demo.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    long saveRole(RoleRequest roleRequest);

    long updateRole(long id, RoleRequest roleRequest);

    List<RoleResponse> getAllRole();

    RoleResponse getRoleById(long id);

    void deleteRoleById(long id);

    boolean isRoleExist(long id);
}
