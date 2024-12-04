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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
