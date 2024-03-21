package org.example.collaboapp.service;

import lombok.AllArgsConstructor;
import org.example.collaboapp.controller.ProjectController;
import org.example.collaboapp.controller.TaskController;
import org.example.collaboapp.dto.Mapper.EntityMapper;
import org.example.collaboapp.dto.TaskRequestDto;
import org.example.collaboapp.dto.TaskResponseDto;
import org.example.collaboapp.model.Task;
import org.example.collaboapp.model.utils.Status;
import org.example.collaboapp.repository.TaskRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final EntityMapper entityMapper;
    @Override
    public TaskResponseDto saveTask(TaskRequestDto taskRequestDto) {
        Task task = entityMapper.taskRequestDtoToTask(taskRequestDto);
        task.setStatus(Status.TODO);
        Task savedTask = taskRepository.save(task);
        return entityMapper.taskToTaskResponseDto(savedTask);
    }

    @Override
    public List<TaskResponseDto> getAllTasks(int page , int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Task> tasks = taskRepository.findAll(pageable).getContent();
        return tasks.stream()
                .map(entityMapper::taskToTaskResponseDto)
                .peek(taskResponseDto -> {
                    Link selfLink = linkTo(methodOn( TaskController.class).getTask(taskResponseDto.getProjectId(),taskResponseDto.getTaskId())).withSelfRel();
                    taskResponseDto.add(selfLink);
                })
                .toList();
    }

    @Override
    public TaskResponseDto getTaskById(int id) {
        Task task = taskRepository.findById((long)id)
                .orElseThrow(() -> new RuntimeException("task not found with given id"));
        Link selfLink = linkTo(methodOn( TaskController.class).getTask(task.getProjectId(),task.getTaskId())).withSelfRel();
        Link deleteLink = linkTo(methodOn( TaskController.class).deleteTask(task.getProjectId(),task.getTaskId())).withRel("delete").withTitle("Endpoint for deleting task");
        return entityMapper.taskToTaskResponseDto(task).add(selfLink,deleteLink);
    }

    @Override
    public TaskResponseDto updateTask(int id , TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById((long)id)
                .orElseThrow(() -> new RuntimeException("task not found with given id"));
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        Task updatedTask = taskRepository.save(task);
        return entityMapper.taskToTaskResponseDto(updatedTask);
    }

    @Override
    public TaskResponseDto deleteTask(int id) {
        Task task = taskRepository.findById((long)id)
                .orElseThrow(() -> new RuntimeException("task not found with given id"));
        taskRepository.delete(task);
        return entityMapper.taskToTaskResponseDto(task);
    }
}
