package com.buihien.demo.services;

import com.buihien.demo.dto.request.DepartmentRequest;
import com.buihien.demo.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    void updateDepartment(long id, DepartmentRequest DepartmentRequest);

    List<DepartmentResponse> getAllDepartment();

    DepartmentResponse getDepartmentById(long id);

    void deleteDepartmentById(long id);

}
