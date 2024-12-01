package com.buihien.demo.services;

import com.buihien.demo.dto.request.CountryRequest;
import com.buihien.demo.dto.response.CountryResponse;

import java.util.List;

public interface CountryService {
    long saveCountry(CountryRequest countryRequest);

    long updateCountry(long id, CountryRequest countryRequest);

    List<CountryResponse> getAllCountries();

    CountryResponse getCountryById(long id);

    void deleteCountryById(long id);
}
