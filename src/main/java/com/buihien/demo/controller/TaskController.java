package com.buihien.demo.controller;

import com.buihien.demo.dto.request.TaskRequest;
import com.buihien.demo.dto.response.generic.ResponseData;
import com.buihien.demo.dto.response.generic.ResponseError;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.services.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/v1/task")
@Validated
@Slf4j
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/")
    public ResponseData<?> addTask(@Valid @RequestBody TaskRequest taskRequest) {
        log.info("Request add task {}", taskRequest.getName());

        try {
            long id = taskService.saveTask(taskRequest);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Task add success", id);
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.CONFLICT.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add Task fail");
        }
    }

    @PutMapping("/{taskId}")
    public ResponseData<?> updateTask(@PathVariable @Min(1) long taskId, @Valid @RequestBody TaskRequest taskRequest) {
        log.info("Request update Task Id={}", taskId);

        try {
            taskService.updateTask(taskId, taskRequest);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Task update success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update Task fail");
        }
    }


    @DeleteMapping("/{taskId}")
    public ResponseData<?> deleteTask(@PathVariable @Min(value = 1, message = "Id Task must be greater than 0") long taskId) {
        log.info("Request delete Task id={}", taskId);

        try {
            taskService.deleteTaskById(taskId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Task delete success");
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete Task fail");
        }
    }

    @GetMapping("/{taskId}")
    public ResponseData<?> getTask(@PathVariable @Min(value = 1, message = "Id Task must be greater than 0") long taskId) {
        log.info("Request get Task by id={}", taskId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get Task by id", taskService.getTaskById(taskId));
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allTask")
    public ResponseData<?> getAllTask() {
        log.info("Request get all Task");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all Task", taskService.getAllTask());
        } catch (ResourceNotFoundException e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/allTaskWithPage")
    public ResponseData<?> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                       @Min(10) @RequestParam(defaultValue = "10", required = false) int pageSize) {
        return new ResponseData<>(HttpStatus.OK.value(), "allTaskWithPage", taskService.getAllTasksWithPage(pageNo, pageSize));
    }


    @GetMapping("/allTaskExcel")
    public ResponseEntity<?> getAllTaskExportExcel() {
        log.info("Request to export all tasks to Excel");
        try {
            ByteArrayInputStream excelFile = taskService.exportAllTasksToExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=all_tasks.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(excelFile));
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage(), e.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to export tasks to Excel"));
        }
    }

    @GetMapping("/advanceSearch")
    public ResponseData<?> advanceSearch(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                         @Min(1) @RequestParam(defaultValue = "1", required = false) int pageSize,
                                         @RequestParam(required = false) String taskName,
                                         @RequestParam(required = false) String personName,
                                         @RequestParam(required = false) String projectName,
                                         @RequestParam(required = false) String sortBy,
                                         @RequestParam(required = false) String companyName) {

        log.info("_____________________________controller____________________");
        return new ResponseData<>(HttpStatus.OK.value(),
                "advanceSearch", taskService.advanceSearch(pageNo, pageSize, taskName, personName, companyName, projectName, sortBy));
    }
}
