package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.CompanyRequest;
import com.buihien.demo.dto.response.CompanyResponse;
import com.buihien.demo.dto.response.DepartmentResponse;
import com.buihien.demo.dto.response.PersonResponse;
import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Company;
import com.buihien.demo.entities.Department;
import com.buihien.demo.entities.Person;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.CompanyRepository;
import com.buihien.demo.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public long saveCompany(CompanyRequest companyRequest) {
        var company = toCompanyEntity(companyRequest);
        company.setDepartments(
                companyRequest.getDepartment()
                        .stream()
                        .map(department -> {
                            return Department.builder()
                                    .code(department.getCode())
                                    .name(department.getName())
                                    .parent(department.getParent())
                                    .build();
                        }).collect(Collectors.toSet())
        );

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

        company.setDepartments(
                companyRequest.getDepartment()
                        .stream()
                        .map(department -> {
                            return Department.builder()
                                    .code(department.getCode())
                                    .name(department.getName())
                                    .parent(department.getParent())
                                    .build();
                        }).collect(Collectors.toSet())
        );

        companyRepository.save(company);
        return company.getId();
    }

    @Override
    public List<CompanyResponse> getAllCompany() {

        return companyRepository.findAll().stream()
                .map(this::mapToCompanyResponse)
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
                        ).collect(Collectors.toSet()))
                .departments(company.getDepartments()
                        .stream().map(department ->
                                DepartmentResponse.builder()
                                        .id(department.getId())
                                        .name(department.getName())
                                        .code(department.getCode())
                                        .build())
                        .collect(Collectors.toSet()))
                .build();
    }


    @Override
    public void deleteCompanyById(long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        companyRepository.delete(company);
    }

    @Override
    public PageResponse<?> getAllCompanyWithPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Company> companies = companyRepository.findAll(pageable);

        List<CompanyResponse> responses = companies.stream()
                .map(this::mapToCompanyResponse)
                .collect(Collectors.toList());
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(companies.getTotalPages())
                .items(responses)
                .build();
    }


    private Company toCompanyEntity(CompanyRequest companyRequest) {
        return Company.builder()
                .name(companyRequest.getName())
                .code(companyRequest.getCode())
                .address(companyRequest.getAddress())
                .build();
    }

    private CompanyResponse mapToCompanyResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .code(company.getCode())
                .address(company.getAddress())
                .persons(mapToPersonResponses(company.getPersons()))
                .departments(mapToDepartmentResponses(company.getDepartments()))
                .build();
    }

    private Set<PersonResponse> mapToPersonResponses(Set<Person> persons) {
        return persons.stream()
                .map(this::mapToPersonResponse)
                .collect(Collectors.toSet());
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .fullName(person.getFullName())
                .gender(person.getGender())
                .birthdate(person.getBirthdate())
                .idCompany(null) // This can be set to null or mapped if necessary
                .build();
    }

    private Set<DepartmentResponse> mapToDepartmentResponses(Set<Department> departments) {
        return departments.stream()
                .map(this::mapToDepartmentResponse)
                .collect(Collectors.toSet());
    }

    private DepartmentResponse mapToDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .build();
    }
}
