package com.buihien.demo.controller;

import com.buihien.demo.dto.request.RoleRequest;
import com.buihien.demo.dto.response.generic.ResponseData;
import com.buihien.demo.dto.response.generic.ResponseError;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.services.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
@Validated
@Slf4j
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/")
    public ResponseData<?> addRole(@Valid @RequestBody RoleRequest RoleRequest) {
        log.info("Request add role {}", RoleRequest);

        try {
            long id = roleService.saveRole(RoleRequest);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Role add success", id);
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.CONFLICT.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add Role fail");
        }
    }

    @PutMapping("/{roleId}")
    public ResponseData<?> updateRole(@PathVariable @Min(1) long roleId, @Valid @RequestBody RoleRequest roleRequest) {
        log.info("Request update role Id={}", roleId);

        try {
            roleService.updateRole(roleId, roleRequest);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Role update success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update role fail");
        }
    }


    @DeleteMapping("/{roleId}")
    public ResponseData<?> deleteRole(@PathVariable @Min(value = 1, message = "Id Role must be greater than 0") long roleId) {
        log.info("Request delete role id={}", roleId);

        try {
            roleService.deleteRoleById(roleId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Role delete success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete role fail");
        }
    }

    @GetMapping("/{roleId}")
    public ResponseData<?> getCountry(@PathVariable @Min(value = 1, message = "Id role must be greater than 0") long roleId) {
        log.info("Request get role by id={}", roleId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get Role", roleService.getRoleById(roleId));
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allRole")
    public ResponseData<?> getAllCountry() {
        log.info("Request get all Role");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all Role", roleService.getAllRole());
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
