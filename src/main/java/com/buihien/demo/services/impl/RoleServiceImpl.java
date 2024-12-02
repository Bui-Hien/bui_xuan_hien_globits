package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.RoleRequest;
import com.buihien.demo.dto.response.RoleResponse;
import com.buihien.demo.entities.Role;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.RoleRepository;
import com.buihien.demo.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public long saveRole(RoleRequest roleRequest) {
        return roleRepository.save(
                        mapToRoleEntity(roleRequest))
                .getId();
    }

    @Override
    public long updateRole(long id, RoleRequest roleRequest) {
        var role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        role.setRole(roleRequest.getRole());
        role.setDescription(roleRequest.getDescription());
        roleRepository.save(role);
        return role.getId();
    }

    @Override
    public List<RoleResponse> getAllRole() {
        var roles = roleRepository.findAll();
        return roles.stream().map(this::mapToRoleResponse).collect(Collectors.toList());
    }

    @Override
    public RoleResponse getRoleById(long id) {
        var role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return mapToRoleResponse(role);
    }

    @Override
    public void deleteRoleById(long id) {
        var role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        roleRepository.delete(role);
    }

    @Override
    public boolean isRoleExist(long id) {
        return roleRepository.existsById(id);
    }

    private Role mapToRoleEntity(RoleRequest roleRequest) {
        return Role.builder()
                .role(roleRequest.getRole())
                .description(roleRequest.getDescription())
                .build();
    }

    private RoleResponse mapToRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .role(role.getRole())
                .description(role.getDescription())
                .build();
    }
}
