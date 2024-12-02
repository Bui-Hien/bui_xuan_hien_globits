package com.buihien.demo.controller;

import com.buihien.demo.dto.request.DepartmentRequest;
import com.buihien.demo.dto.response.generic.ResponseData;
import com.buihien.demo.dto.response.generic.ResponseError;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.services.DepartmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/department")
@Validated
@Slf4j
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PutMapping("/{departmentId}")
    public ResponseData<?> updateDepartment(@PathVariable @Min(1) long departmentId, @Valid @RequestBody DepartmentRequest departmentRequest) {
        log.info("Request update department Id={}", departmentId);

        try {
            departmentService.updateDepartment(departmentId, departmentRequest);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Department update success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update department fail");
        }
    }


    @DeleteMapping("/{departmentId}")
    public ResponseData<?> deleteDepartment(@PathVariable @Min(value = 1, message = "Id Department must be greater than 0") long departmentId) {
        log.info("Request delete departmentId={}", departmentId);

        try {
            departmentService.deleteDepartmentById(departmentId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Department delete success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete Department fail");
        }
    }

    @GetMapping("/{departmentId}")
    public ResponseData<?> getdepartment(@PathVariable @Min(value = 1, message = "Id Department must be greater than 0") long departmentId) {
        log.info("Request get department by id={}", departmentId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get Department", departmentService.getDepartmentById(departmentId));
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allDepartment")
    public ResponseData<?> getAllDepartment() {
        log.info("Request get all department");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all department", departmentService.getAllDepartment());
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
