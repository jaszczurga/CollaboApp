package org.example.collaboapp.dto.Mapper;

import org.example.collaboapp.dto.Authentication.UserResponseDto;
import org.example.collaboapp.dto.ProjectRequestDto;
import org.example.collaboapp.dto.ProjectResponseDto;
import org.example.collaboapp.dto.TaskRequestDto;
import org.example.collaboapp.dto.TaskResponseDto;
import org.example.collaboapp.model.Project;
import org.example.collaboapp.model.Task;
import org.example.collaboapp.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityMapper {

    //converting from Entity to responseDto
    public ProjectResponseDto projectToProjectResponseDto(Project project) {
        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .title(project.getTitle())
                .description(project.getDescription())
                .manager(project.getManager() != null ? userToUserResponseDto(project.getManager()) : null)
                .users(project.getUsers() != null ? project.getUsers().stream().map(this::userToUserResponseDto).collect( Collectors.toSet()) : null)
                .build();
    }

    //converting from requestDto to Entity
    public Project projectRequestDtoToProject(ProjectRequestDto projectRequestDto) {
        return Project.builder()
                .title(projectRequestDto.getTitle())
                .description(projectRequestDto.getDescription())
                .build();
    }

    //converting from taskrequestDto to task
    public Task taskRequestDtoToTask(TaskRequestDto taskRequestDto) {
        return Task.builder()
                .title(taskRequestDto.getTitle())
                .description(taskRequestDto.getDescription())
                .projectId(taskRequestDto.getProjectId())
                .build();
    }

    //converting from task to taskResponseDto
    public TaskResponseDto taskToTaskResponseDto(Task task) {
        return TaskResponseDto.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .projectId(task.getProjectId())
                .status(task.getStatus().name())
                .assignee(task.getUser() != null ? userToUserResponseDto(task.getUser()) : null)
                .build();
    }

    public UserResponseDto userToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .firstName( user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRoles())
                .build();
    }




}
