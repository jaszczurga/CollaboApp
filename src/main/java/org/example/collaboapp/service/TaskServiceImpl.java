package org.example.collaboapp.service;

import lombok.AllArgsConstructor;
import org.example.collaboapp.controller.ProjectController;
import org.example.collaboapp.controller.TaskController;
import org.example.collaboapp.dto.ListResponseDto;
import org.example.collaboapp.dto.Mapper.EntityMapper;
import org.example.collaboapp.dto.TaskRequestDto;
import org.example.collaboapp.dto.TaskResponseDto;
import org.example.collaboapp.exception.NotAllowedException;
import org.example.collaboapp.exception.NotFoundException;
import org.example.collaboapp.model.Project;
import org.example.collaboapp.model.Task;
import org.example.collaboapp.model.utils.Status;
import org.example.collaboapp.repository.ProjectRepository;
import org.example.collaboapp.repository.TaskRepository;
import org.example.collaboapp.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    @Override
    public TaskResponseDto saveTask(TaskRequestDto taskRequestDto) {
        Task task = entityMapper.taskRequestDtoToTask(taskRequestDto);
        task.setStatus(Status.TODO);
        Task savedTask = taskRepository.save(task);
        return entityMapper.taskToTaskResponseDto(savedTask);
    }

    @Override
    public ListResponseDto getAllTasks(int page , int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Task> tasks = taskRepository.findAll(pageable).getContent();
        List resultTasks =  tasks.stream()
                .map(entityMapper::taskToTaskResponseDto)
                .peek(taskResponseDto -> {
                    Link selfLink = linkTo(methodOn( TaskController.class).getTask(taskResponseDto.getProjectId(),taskResponseDto.getTaskId())).withSelfRel();
                    taskResponseDto.add(selfLink);
                })
                .toList();
        ListResponseDto listResponseDto = new ListResponseDto();
        listResponseDto.setContent(resultTasks);
        Link selfLink = linkTo(methodOn( TaskController.class).getTasks(tasks.getFirst().getProjectId(),0,100)).withSelfRel();
        Link addTask = linkTo(methodOn( TaskController.class).saveTask(tasks.getFirst().getProjectId(),null)).withRel("addTask").withTitle("Endpoint for adding task");
        listResponseDto.add(selfLink,addTask);
        return listResponseDto;
    }

    @Override
    public TaskResponseDto getTaskById(int id) {
        Task task = taskRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException("task not found with given id"));
        Link selfLink = linkTo(methodOn( TaskController.class).getTask(task.getProjectId(),task.getTaskId())).withSelfRel();
        Link deleteLink = linkTo(methodOn( TaskController.class).deleteTask(task.getProjectId(),task.getTaskId())).withRel("delete").withTitle("Endpoint for deleting task");
        Link updateLink = linkTo(methodOn( TaskController.class).updateTask(task.getProjectId(),task.getTaskId(),null)).withRel("update").withTitle("Endpoint for updating task necessary filelds title and description");
        Link assignUserLink = linkTo(methodOn( TaskController.class).assignUserToTask(task.getProjectId(),task.getTaskId(),0)).withRel("assignUser").withTitle("Endpoint for assigning user to task");
        Link removeUserLink = linkTo(methodOn( TaskController.class).removeUserFromTask(task.getProjectId(),task.getTaskId(),0)).withRel("removeUser").withTitle("Endpoint for removing user from task");
        Link changeStatusLink = linkTo(methodOn( TaskController.class).changeTaskStatus(task.getProjectId(),task.getTaskId(),0)).withRel("changeStatus").withTitle("Endpoint for changing task status 0 => _TODO, 1 => IN_PROGRESS, 2 => DONE");
        return entityMapper.taskToTaskResponseDto(task).add(selfLink,deleteLink,updateLink,assignUserLink,removeUserLink,changeStatusLink);
    }

    @Override
    public TaskResponseDto updateTask(int id , TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException("task not found with given id"));
        task.setTitle(taskRequestDto.getTitle() != null ? taskRequestDto.getTitle() : task.getTitle());
        task.setDescription(taskRequestDto.getDescription() != null ? taskRequestDto.getDescription() : task.getDescription());
        Task updatedTask = taskRepository.save(task);
        return entityMapper.taskToTaskResponseDto(updatedTask);
    }

    @Override
    public TaskResponseDto deleteTask(int id) {
        Task task = taskRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException("task not found with given id"));
        taskRepository.delete(task);
        return entityMapper.taskToTaskResponseDto(task);
    }

    @Override
    public TaskResponseDto assignUserToTask(int id , int userId) {
        Task task = taskRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException("task not found with given id"));
        task.setUser( userRepository.findById((long)userId)
                .orElseThrow(() -> new NotFoundException("user not found with given id")));
        Project project = projectRepository.findById((long)task.getProjectId())
                .orElseThrow(() -> new NotFoundException("project not found with given id"));

        if(project.getUsers().stream().noneMatch(user -> user.getUserId() == userId)){
            throw new NotAllowedException("user not assigned to project");
        }

        Task updatedTask = taskRepository.save(task);
        TaskResponseDto taskResponseDto = entityMapper.taskToTaskResponseDto(updatedTask);
        taskResponseDto.setAssignee( entityMapper.userToUserResponseDto(updatedTask.getUser()));
        return  taskResponseDto;
    }

    @Override
    public TaskResponseDto removeUserFromTask(int id , int userId) {
        Task task = taskRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException("task not found with given id"));
        task.setUser(null);
        Task updatedTask = taskRepository.save(task);
        TaskResponseDto taskResponseDto = entityMapper.taskToTaskResponseDto(updatedTask);
        taskResponseDto.setAssignee(null);
        return taskResponseDto;
    }

    @Override
    public TaskResponseDto changeTaskStatus(int id , int statusState) {
        Task task = taskRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException("task not found with given id"));
        //0 => _TODO, 1 => IN_PROGRESS, 2 => DONE
        task.setStatus(Status.values()[statusState]);
        Task updatedTask = taskRepository.save(task);
        return entityMapper.taskToTaskResponseDto(updatedTask);
    }


}
