package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.CountryRequest;
import com.buihien.demo.dto.response.CountryResponse;
import com.buihien.demo.entities.Country;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.CountryRepository;
import com.buihien.demo.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public long saveCountry(CountryRequest countryRequest) {
        Country country = toCountryEntity(countryRequest);
        countryRepository.save(country);
        return country.getId();
    }

    @Override
    public long updateCountry(long id, CountryRequest countryRequest) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        country.setName(countryRequest.getName());
        country.setCode(countryRequest.getCode());
        country.setDescription(countryRequest.getDescription());
        countryRepository.save(country);
        return country.getId();
    }

    @Override
    public List<CountryResponse> getAllCountry() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
                .map(this::toCountryResponse).collect(Collectors.toList());
    }

    @Override
    public CountryResponse getCountryById(long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        return toCountryResponse(country);
    }

    @Override
    public void deleteCountryById(long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        countryRepository.delete(country);

    }

    private Country toCountryEntity(CountryRequest countryRequest) {
        return Country.builder()
                .name(countryRequest.getName())
                .code(countryRequest.getCode())
                .description(countryRequest.getDescription())
                .build();
    }

    private CountryResponse toCountryResponse(Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .code(country.getCode())
                .description(country.getDescription())
                .build();
    }

}
