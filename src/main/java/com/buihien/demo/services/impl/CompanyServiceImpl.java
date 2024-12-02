package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.CompanyRequest;
import com.buihien.demo.dto.response.CompanyResponse;
import com.buihien.demo.dto.response.PersonResponse;
import com.buihien.demo.entities.Company;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.CompanyRepository;
import com.buihien.demo.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<CompanyResponse> getAllCompany() {
        return companyRepository.findAll().stream()
                .map(company -> CompanyResponse.builder()  // Sửa lại để trả về đối tượng
                        .id(company.getId())
                        .name(company.getName())
                        .code(company.getCode())
                        .address(company.getAddress())
                        .persons(company.getPersons()
                                .stream().map(person -> PersonResponse.builder()
                                        .id(person.getId())
                                        .fullName(person.getFullName())
                                        .gender(person.getGender())
                                        .birthdate(person.getBirthdate())
                                        .idCompany(null)
                                        .build()
                                ).collect(Collectors.toList()))  // Collect lại danh sách các PersonResponse
                        .build())  // Trả về đối tượng CompanyResponse
                .collect(Collectors.toList());
    }

    @Override
    public CompanyResponse getCompanyById(long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .code(company.getCode())
                .address(company.getAddress())
                .persons(company.getPersons()
                        .stream().map(person -> PersonResponse.builder()
                                .id(person.getId())
                                .fullName(person.getFullName())
                                .gender(person.getGender())
                                .birthdate(person.getBirthdate())
                                .idCompany(null)
                                .build()
                        ).collect(Collectors.toList()))
                .build();
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
