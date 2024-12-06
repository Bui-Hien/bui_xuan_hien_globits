package com.buihien.demo.services.impl;

import com.buihien.demo.dto.request.TaskRequest;
import com.buihien.demo.dto.response.CompanyResponse;
import com.buihien.demo.dto.response.PersonResponse;
import com.buihien.demo.dto.response.ProjectResponse;
import com.buihien.demo.dto.response.TaskResponse;
import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Project;
import com.buihien.demo.entities.Task;
import com.buihien.demo.exception.ResourceNotFoundException;
import com.buihien.demo.repository.TaskRepository;
import com.buihien.demo.repository.SearchRepository;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PersonService personService;
    private final ProjectService projectService;
    private final SearchRepository searchRepository;

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

        task.setName(taskRequest.getName());
        task.setDescription(taskRequest.getDescription());
        task.setStartTime(taskRequest.getStartTime());
        task.setEndTime(taskRequest.getEndTime());
        task.setPriority(Priority.valueOf(taskRequest.getPriority()));
        task.setStatus(Status.valueOf(taskRequest.getStatus()));
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
    public TaskResponse getTaskById(long id) {
        var task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return TaskResponse.builder()
                .id(task.getId())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .priority(String.valueOf(task.getPriority()))
                .name(task.getName())
                .description(task.getDescription())
                .status(String.valueOf(task.getStatus()))
                .project(
                        getBuild(task)
                )
                .person(getPersonResponse(task)
                )
                .company(getCompanyResponse(task))
                .build();
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
        List<TaskResponse> taskResponse = tasks.stream().map(task -> TaskResponse.builder()
                        .id(task.getId())
                        .startTime(task.getStartTime())
                        .endTime(task.getEndTime())
                        .priority(String.valueOf(task.getPriority()))
                        .name(task.getName())
                        .description(task.getDescription())
                        .status(String.valueOf(task.getStatus()))
                        .project(
                                getBuild(task)
                        )
                        .person(getPersonResponse(task)
                        )
                        .company(getCompanyResponse(task))
                        .build())
                .collect(Collectors.toList());
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(tasks.getTotalPages())
                .items(taskResponse)
                .build();
    }

    @Override
    public PageResponse<?> getAllTasksWithPageFindField(int pageNo, int pageSize, String taskName, String personName, String companyName, String projectName) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> tasks = taskRepository.findAll(pageable);

        // Filter and transform tasks into TaskResponse
        List<TaskResponse> taskResponses = tasks.stream()
                .filter(task -> task.getName().equals(taskName)
                        && task.getPerson().getFullName().equals(personName)
                        && task.getProject().getName().equals(projectName)
                        && task.getProject().getCompany().getName().equals(companyName))
                .map(task -> TaskResponse.builder()
                        .id(task.getId())
                        .startTime(task.getStartTime())
                        .endTime(task.getEndTime())
                        .priority(String.valueOf(task.getPriority()))
                        .name(task.getName())
                        .description(task.getDescription())
                        .status(String.valueOf(task.getStatus()))
                        .project(
                                getBuild(task)
                        )
                        .person(getPersonResponse(task)
                        )
                        .company(getCompanyResponse(task))
                        .build())
                .collect(Collectors.toList());

        // Build and return the paginated response
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage((int) Math.ceil((double) taskResponses.size() / pageSize))
                .items(taskResponses)
                .build();
    }

    private static CompanyResponse getCompanyResponse(Task task) {
        return CompanyResponse.builder()
                .id(task.getProject().getCompany().getId())
                .name(task.getProject().getCompany().getName())
                .code(task.getProject().getCompany().getCode())
                .address(task.getProject().getCompany().getAddress())
                .build();
    }

    private static PersonResponse getPersonResponse(Task task) {
        return PersonResponse.builder()
                .id(task.getPerson().getId())
                .gender(task.getPerson().getGender())
                .fullName(task.getPerson().getFullName())
                .birthdate(task.getPerson().getBirthdate()).build();
    }

    private static ProjectResponse getBuild(Task task) {
        return ProjectResponse.builder()
                .id(task.getProject().getId())
                .code(task.getProject().getCode())
                .name(task.getProject().getName())
                .build();
    }


    @Override
    public PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String... search) {
//        return searchRepository.advanceSearch(pageNo, pageSize, search);

        return null;
    }

    @Override
    public PageResponse<?> searchTasks(String nameTask, String nameProject, String nameCompany, String namePerson) {
        return searchRepository.searchTasks(nameTask, nameProject, nameCompany, namePerson);
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
                Row row = sheet.createRow(rowIdx);
                row.createCell(0).setCellValue(rowIdx);
                row.createCell(1).setCellValue(task.getStartTime().toString());
                row.createCell(2).setCellValue(task.getEndTime().toString());
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
