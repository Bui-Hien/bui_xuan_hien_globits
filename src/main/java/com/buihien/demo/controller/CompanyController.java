package com.buihien.demo.controller;

import com.buihien.demo.dto.request.CompanyRequest;
import com.buihien.demo.dto.request.UserRequest;
import com.buihien.demo.dto.response.generic.ResponseData;
import com.buihien.demo.dto.response.generic.ResponseError;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.services.CompanyService;
import com.buihien.demo.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/")
    public ResponseData<?> addUser(@Valid @RequestBody CompanyRequest companyRequest) {
        log.info("Request add country {}", companyRequest);

        try {
            long id = companyService.saveCompany(companyRequest);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Company add success", id);
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.CONFLICT.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add company fail");
        }
    }

    @PutMapping("/{companyId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) long companyId, @Valid @RequestBody CompanyRequest companyRequest) {
        log.info("Request update company Id={}", companyId);

        try {
            companyService.updateCompany(companyId, companyRequest);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Company update success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }
    }


    @DeleteMapping("/{companyId}")
    public ResponseData<?> deleteUser(@PathVariable @Min(value = 1, message = "Id user must be greater than 0") long companyId) {
        log.info("Request delete ucompanyId={}", companyId);

        try {
            companyService.deleteCompanyById(companyId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Company delete success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }
    }

    @GetMapping("/{companyId}")
    public ResponseData<?> getCountry(@PathVariable @Min(value = 1, message = "Id company must be greater than 0") long companyId) {
        log.info("Request get company by id={}", companyId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get company", companyService.getCompanyById(companyId));
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allCompany")
    public ResponseData<?> getAllCountry() {
        log.info("Request get all company");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all company", companyService.getAllCountry());
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}