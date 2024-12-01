package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.CompanyRequest;
import com.buihien.demo.entities.Company;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.CompanyRepository;
import com.buihien.demo.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public long saveCompany(CompanyRequest countryRequest) {
        var company = toCompanyEntity(countryRequest);
        companyRepository.save(company);
        return company.getId();
    }

    @Override
    public long updateCompany(long id, CompanyRequest companyRequest) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        company.setName(companyRequest.getName());
        company.setAddress(companyRequest.getAddress());
        company.setCode(companyRequest.getCode());
        companyRepository.save(company);
        return company.getId();
    }

    @Override
    public List<Company> getAllCountry() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    @Override
    public void deleteCompanyById(long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        companyRepository.delete(company);
    }



    private Company toCompanyEntity(CompanyRequest companyRequest) {
        return Company.builder()
                .name(companyRequest.getName())
                .code(companyRequest.getCode())
                .address(companyRequest.getAddress())
                .build();
    }

}
