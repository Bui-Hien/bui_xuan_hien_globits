package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.DepartmentRequest;
import com.buihien.demo.dto.response.CompanyResponse;
import com.buihien.demo.dto.response.DepartmentResponse;
import com.buihien.demo.entities.Company;
import com.buihien.demo.entities.Department;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.DepartmentRepository;
import com.buihien.demo.services.CompanyService;
import com.buihien.demo.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final CompanyService companyService;

    @Override
    public void updateDepartment(long id, DepartmentRequest departmentRequest) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        department.setName(departmentRequest.getName());
        department.setCode(departmentRequest.getCode());
        department.setParent(departmentRequest.getParent());
        var companyId = companyService.getCompanyById(department.getCompany().getId()).getId();
        department.setCompany(
                Company.builder()
                        .id(companyId)
                        .build()
        );
        departmentRepository.save(department);
    }

    @Override
    public List<DepartmentResponse> getAllDepartment() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToDepartmentResponse).collect(Collectors.toList());

    }

    @Override
    public DepartmentResponse getDepartmentById(long id) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToDepartmentResponse(department);
    }

    @Override
    public void deleteDepartmentById(long id) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(department);
    }

    private Department mapToDepartmentEntity(DepartmentRequest departmentRequest) {
        return Department.builder()
                .code(departmentRequest.getCode())
                .name(departmentRequest.getName())
                .parent(departmentRequest.getParent())
                .build();
    }


    private DepartmentResponse mapToDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .company(CompanyResponse.builder()
                        .id(department.getCompany().getId())
                        .name(department.getCompany().getName())
                        .code(department.getCompany().getCode())
                        .address(department.getCompany().getAddress())
                        .build())
                .build();
    }
}
