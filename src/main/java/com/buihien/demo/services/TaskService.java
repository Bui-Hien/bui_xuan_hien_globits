package com.buihien.demo.services;

import com.buihien.demo.dto.request.TaskRequest;
import com.buihien.demo.dto.response.generic.PageResponse;
import com.buihien.demo.entities.Task;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface TaskService {
    long saveTask(TaskRequest TaskRequest);

    void updateTask(long id, TaskRequest TaskRequest);

    List<Task> getAllTask();

    Task getTaskById(long id);

    void deleteTaskById(long id);

    PageResponse<?> getAllTasksWithPage(int pageNo, int pageSize);

    ByteArrayInputStream exportAllTasksToExcel();

}
