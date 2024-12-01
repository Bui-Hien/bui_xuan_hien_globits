package com.buihien.demo.controller;

import com.buihien.demo.dto.request.CountryRequest;
import com.buihien.demo.dto.response.generic.ResponseData;
import com.buihien.demo.dto.response.generic.ResponseError;
import com.buihien.demo.services.CountryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/country")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @PostMapping("/")
    public ResponseData<?> addUser(@Valid @RequestBody CountryRequest countryRequest) {
        log.info("Request add country {}", countryRequest);

        try {
            long id = countryService.saveCountry(countryRequest);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Country add success", id);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add country fail");
        }
    }

    @PutMapping("/{countryId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) long countryId, @Valid @RequestBody CountryRequest countryRequest) {
        log.info("Request update country Id={}", countryId);

        try {
            countryService.updateCountry(countryId, countryRequest);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Country update success");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update country fail");
        }
    }


    @DeleteMapping("/{countryId}")
    public ResponseData<?> deleteUser(@PathVariable @Min(value = 1, message = "countryId must be greater than 0") long countryId) {
        log.info("Request delete countryId={}", countryId);

        try {
            countryService.deleteCountryById(countryId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Country delete success");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete country fail");
        }
    }

    @GetMapping("/{countryId}")
    public ResponseData<?> getCountry(@PathVariable @Min(value = 1, message = "countryId must be greater than 0") long countryId) {
        log.info("Request get country by id={}", countryId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get country", countryService.getCountryById(countryId));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allCountry")
    public ResponseData<?> getAllCountry() {
        log.info("Request get all country");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all country", countryService.getAllCountries());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
