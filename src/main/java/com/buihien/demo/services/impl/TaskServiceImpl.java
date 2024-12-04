package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.TaskRequest;
import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Project;
import com.buihien.demo.entities.Task;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.TaskRepository;
import com.buihien.demo.services.*;
import com.buihien.demo.util.Priority;
import com.buihien.demo.util.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PersonService personService;
    private final ProjectService projectService;

    @Override
    public long saveTask(TaskRequest TaskRequest) {
        var task = mapToTaskEntity(TaskRequest);
        var project = projectService.getProjectById(TaskRequest.getProjectId());
        var person = personService.getPersonById(TaskRequest.getPersonId());
        log.info(project.getCompany().getId() + " = " + person.getCompany().getId());
        if (!Objects.equals(project.getCompany().getId(), person.getCompany().getId())) {
            throw new ResourceNotFoundException("Project and person do not belong to the same company.");
        }

        task.setPerson(personService.getPersonById(person.getId()));


        task.setProject(Project.builder()
                .id(project.getId())
                .build());
        taskRepository.save(task);
        return task.getId();
    }

    @Override
    public void updateTask(long id, TaskRequest taskRequest) {
        var task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        var project = projectService.getProjectById(taskRequest.getProjectId());
        var person = personService.getPersonById(taskRequest.getPersonId());

        if (!Objects.equals(project.getCompany().getId(), person.getCompany().getId())) {
            throw new ResourceNotFoundException("Project and person do not belong to the same company.");
        }
        task.setPerson(person);
        task.setProject(Project.builder()
                .id(project.getId())
                .build());
        taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    public void deleteTaskById(long id) {
        var task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
    }


    @Override
    public PageResponse<?> getAllTasksWithPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> tasks = taskRepository.findAll(pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(tasks.getTotalPages())
                .items(tasks.getContent())
                .build();
    }

    @Override
    public ByteArrayInputStream exportAllTasksToExcel() {
        List<Task> tasks = taskRepository.findAll();

        // Tạo workbook và sheet
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Tasks");

            // Tạo header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"STT", "Task start time", "Task end time", "Task priority", "Task name",
                    "Task description", "Task status", "Project code", "Project name",
                    "Project description", "Person full name", "Person gender",
                    "Person birth date", "Person phone number", "Person address"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Thêm dữ liệu
            int rowIdx = 1; // Bắt đầu từ dòng 1 (dòng 0 là header)
            for (Task task : tasks) {
                String startTime = task.getStartTime() != null
                        ? String.valueOf(LocalDateTime.parse(task.getStartTime().toString().substring(0, 19), formatter))
                        : "N/A";

                String endTime = task.getEndTime() != null
                        ? String.valueOf(LocalDateTime.parse(task.getEndTime().toString().substring(0, 19), formatter))
                        : "N/A";
                Row row = sheet.createRow(rowIdx);
                row.createCell(0).setCellValue(rowIdx);
                row.createCell(1).setCellValue(startTime);
                row.createCell(2).setCellValue(endTime);
                row.createCell(3).setCellValue(String.valueOf(task.getPriority()));
                row.createCell(4).setCellValue(task.getName());
                row.createCell(5).setCellValue(task.getDescription());
                row.createCell(6).setCellValue(task.getStatus().toString());

                row.createCell(7).setCellValue(task.getProject().getCode());
                row.createCell(8).setCellValue(task.getProject().getName());
                row.createCell(9).setCellValue(task.getProject().getDescription());

                row.createCell(10).setCellValue(task.getPerson().getFullName());
                row.createCell(11).setCellValue(task.getPerson().getGender());
                row.createCell(12).setCellValue(task.getPerson().getBirthdate().toString());
                row.createCell(13).setCellValue(task.getPerson().getPhoneNumber());
                row.createCell(14).setCellValue(task.getPerson().getAddress());

                rowIdx++;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    private Task mapToTaskEntity(TaskRequest taskRequest) {
        return Task.builder()
                .startTime(taskRequest.getStartTime())
                .endTime(taskRequest.getEndTime())
                .priority(Priority.valueOf(taskRequest.getPriority()))
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .status(Status.valueOf(taskRequest.getStatus()))
                .build();
    }
}
