package org.example.collaboapp.service;

import org.example.collaboapp.dto.ListResponseDto;
import org.example.collaboapp.dto.TaskRequestDto;
import org.example.collaboapp.dto.TaskResponseDto;
import org.example.collaboapp.model.Task;

import java.util.List;

public interface TaskService {

    TaskResponseDto saveTask(TaskRequestDto taskRequestDto);

    ListResponseDto getAllTasks(int page, int size);

    TaskResponseDto getTaskById(int id);

    TaskResponseDto updateTask(int id, TaskRequestDto taskRequestDto);

    TaskResponseDto deleteTask(int id);

    TaskResponseDto assignUserToTask(int id, int userId);

    TaskResponseDto removeUserFromTask(int id, int userId);

    TaskResponseDto changeTaskStatus(int id, int statusState);



}
