package com.buihien.demo.services;

import com.buihien.demo.dto.request.TaskRequest;
import com.buihien.demo.dto.response.TaskResponse;
import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Task;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface TaskService {
    long saveTask(TaskRequest TaskRequest);

    void updateTask(long id, TaskRequest TaskRequest);

    List<Task> getAllTask();

    TaskResponse getTaskById(long id);

    void deleteTaskById(long id);

    PageResponse<?> getAllTasksWithPage(int pageNo, int pageSize);

    PageResponse<?> advanceSearch(int pageNo, int pageSize, String taskName, String personName, String companyName, String projectName, String sortBy);


    ByteArrayInputStream exportAllTasksToExcel();

}
