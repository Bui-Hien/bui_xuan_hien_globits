package com.buihien.demo.services;

import com.buihien.demo.dto.request.ProjectRequest;
import com.buihien.demo.dto.response.ProjectResponse;
import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Project;

import java.util.List;

public interface ProjectService {
    long saveProject(ProjectRequest ProjectRequest);

    void updateProject(long id, ProjectRequest ProjectRequest);

    List<ProjectResponse> getAllProject();

    ProjectResponse getProjectById(long id);

    void deleteProjectById(long id);

    boolean existsByCode(String code);

    PageResponse<?> getAllProjectsWithPage(int pageNo, int pageSize);

}
