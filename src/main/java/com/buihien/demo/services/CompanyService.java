package com.buihien.demo.services;

import com.buihien.demo.dto.request.CompanyRequest;
import com.buihien.demo.dto.request.CountryRequest;
import com.buihien.demo.dto.response.CountryResponse;
import com.buihien.demo.entities.Company;

import java.util.List;

public interface CompanyService {
    long saveCompany(CompanyRequest countryRequest);

    long updateCompany(long id, CompanyRequest companyRequest);

    List<Company> getAllCountry();

    Company getCompanyById(long id);

    void deleteCompanyById(long id);

}
