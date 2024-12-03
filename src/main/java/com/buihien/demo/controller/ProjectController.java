package com.buihien.demo.controller;

import com.buihien.demo.dto.request.ProjectRequest;
import com.buihien.demo.dto.response.generic.ResponseData;
import com.buihien.demo.dto.response.generic.ResponseError;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.services.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/")
    public ResponseData<?> addProject(@Valid @RequestBody ProjectRequest projectRequest) {
        log.info("Request add country {}", projectRequest);

        try {
            long id = projectService.saveProject(projectRequest);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Project add success", id);
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.CONFLICT.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add project fail");
        }
    }

    @PutMapping("/{projectId}")
    public ResponseData<?> updateProject(@PathVariable @Min(1) long projectId, @Valid @RequestBody ProjectRequest projectRequest) {
        log.info("Request update project Id={}", projectId);

        try {
            projectService.updateProject(projectId, projectRequest);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Project update success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update project fail");
        }
    }


    @DeleteMapping("/{projectId}")
    public ResponseData<?> deleteProject(@PathVariable @Min(value = 1, message = "Id Project must be greater than 0") long projectId) {
        log.info("Request delete project id={}", projectId);

        try {
            projectService.deleteProjectById(projectId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Project delete success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete project fail");
        }
    }

    @GetMapping("/{projectId}")
    public ResponseData<?> getCountry(@PathVariable @Min(value = 1, message = "Id Project must be greater than 0") long projectId) {
        log.info("Request get project by id={}", projectId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get Project", projectService.getProjectById(projectId));
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allProject")
    public ResponseData<?> getAllCountry() {
        log.info("Request get all project");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all project", projectService.getAllProject());
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allProjectWithPage")
    public ResponseData<?> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                       @Min(10) @RequestParam(defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "allProjectWithPage", projectService.getAllProjectsWithPage(pageNo, pageSize));
    }
}
