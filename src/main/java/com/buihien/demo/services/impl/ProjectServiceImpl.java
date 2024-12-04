package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.ProjectRequest;
import com.buihien.demo.dto.response.CompanyResponse;
import com.buihien.demo.dto.response.PersonResponse;
import com.buihien.demo.dto.response.ProjectResponse;
import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Company;
import com.buihien.demo.entities.Person;
import com.buihien.demo.entities.Project;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.ProjectRepository;
import com.buihien.demo.services.CompanyService;
import com.buihien.demo.services.PersonService;
import com.buihien.demo.services.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final CompanyService companyService;
    private final PersonService personService;

    @Override
    public long saveProject(ProjectRequest projectRequest) {
        if (existsByCode(projectRequest.getCode())) {
            throw new ResourceNotFoundException("Project already exists");
        }
        var project = mapToProjectEntity(projectRequest);
        long companyId = companyService.getCompanyById(projectRequest.getCompanyId()).getId();
        project.setCompany(Company.builder()
                .id(companyId)
                .build());
        projectRequest.getPersonIds().forEach(personId -> {
            var person = personService.getPersonById(personId);
            if (companyId != person.getCompany().getId())
                throw new ResourceNotFoundException("The person does not belong to the specified company.");
            project.getPersons().add(person);
        });
        projectRepository.save(project);
        return project.getId();
    }

    @Override
    public void updateProject(long id, ProjectRequest projectRequest) {
        var project = projectRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("The project does not exist."));

        if (existsByCode(projectRequest.getCode()) && !project.getCode().equals(projectRequest.getCode())) {
            throw new ResourceNotFoundException("Project code already exists");
        }
        long companyId = companyService.getCompanyById(projectRequest.getCompanyId()).getId();
        project.getCompany().setId(companyId);

        projectRequest.getPersonIds().forEach(personId -> {
            var person = personService.getPersonById(personId);
            if (companyId != person.getCompany().getId())
                throw new ResourceNotFoundException("The person does not belong to the specified company.");
            project.getPersons().add(person);
        });

        projectRepository.save(project);
    }

    @Override
    public List<ProjectResponse> getAllProject() {
        var projects = projectRepository.findAll();

        return projects.stream()
                .map(this::mapToProjectResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getProjectById(long id) {
        var project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The project does not exist."));

        return mapToProjectResponse(project);
    }

    @Override
    public void deleteProjectById(long id) {
        var project = projectRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("The project does not exist."));
        projectRepository.delete(project);
    }

    @Override
    public boolean existsByCode(String code) {
        return projectRepository.existsByCode(code);
    }

    @Override
    public PageResponse<?> getAllProjectsWithPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Project> projects = projectRepository.findAll(pageable);

        List<ProjectResponse> responses = projects.stream()
                .map(this::mapToProjectResponse)
                .collect(Collectors.toList());

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(projects.getTotalPages())
                .items(responses)
                .build();
    }

    private Project mapToProjectEntity(ProjectRequest ProjectRequest) {
        return Project.builder()
                .code(ProjectRequest.getCode())
                .name(ProjectRequest.getName())
                .description(ProjectRequest.getDescription())
                .build();
    }

    private ProjectResponse mapToProjectResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .code(project.getCode())
                .name(project.getName())
                .description(project.getDescription())
                .company(mapToCompanyResponse(project.getCompany())) // Reuse helper for CompanyResponse
                .persons(project.getPersons().stream()
                        .map(this::mapToPersonResponse) // Reuse helper for PersonResponse
                        .collect(Collectors.toSet()))
                .build();
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .fullName(person.getFullName())
                .gender(person.getGender())
                .birthdate(person.getBirthdate())
                .build();
    }

    private CompanyResponse mapToCompanyResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .code(company.getCode())
                .address(company.getAddress())
                .build();
    }

}

