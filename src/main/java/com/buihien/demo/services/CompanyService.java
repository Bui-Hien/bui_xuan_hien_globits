package com.buihien.demo.services;

import com.buihien.demo.dto.request.CompanyRequest;
import com.buihien.demo.dto.response.CompanyResponse;
import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Company;

import java.util.List;

public interface CompanyService {
    long saveCompany(CompanyRequest countryRequest);

    void updateCompany(long id, CompanyRequest companyRequest);

    List<CompanyResponse> getAllCompany();

    CompanyResponse getCompanyById(long id);

    Company getCompanyByIdE(long id);

    void deleteCompanyById(long id);

    PageResponse<?> getAllCompanyWithPage(int pageNo, int pageSize);

}
