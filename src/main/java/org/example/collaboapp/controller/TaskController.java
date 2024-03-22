package org.example.collaboapp.controller;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.example.collaboapp.dto.Mapper.EntityMapper;
import org.example.collaboapp.dto.TaskRequestDto;
import org.example.collaboapp.dto.TaskResponseDto;
import org.example.collaboapp.model.User;
import org.example.collaboapp.repository.UserRepository;
import org.example.collaboapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RestController
@RequestMapping("/api/projects/{id}")
public class TaskController {

    private final TaskService taskService;
    private final EntityMapper entityMapper;
    private final UserRepository userRepository;

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(@PathVariable int id,
                                                          @RequestParam(defaultValue = "0",required = false) int page,
                                                          @RequestParam(defaultValue = "100",required = false) int size) {
        return ResponseEntity.ok(taskService.getAllTasks(page, size));
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable int id, @PathVariable int taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @PostMapping("/saveTask")
    public ResponseEntity<TaskResponseDto> saveTask(@PathVariable int id,@RequestBody TaskRequestDto taskRequestDto) {
        taskRequestDto.setProjectId(id);
        return ResponseEntity.status( HttpStatus.CREATED ).body(taskService.saveTask(taskRequestDto));
    }

    @PutMapping("/updateTask/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable int id, @PathVariable int taskId, @RequestBody TaskRequestDto taskRequestDto) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskRequestDto));
    }

    @DeleteMapping("/deleteTask/{taskId}")
    public ResponseEntity<TaskResponseDto> deleteTask(@PathVariable int id,@PathVariable int taskId) {
        return ResponseEntity.ok(taskService.deleteTask(taskId));
    }

    @PutMapping("/assignUserToTask/{taskId}")
    public ResponseEntity<TaskResponseDto> assignUserToTask(@PathVariable int id, @PathVariable int taskId,@RequestParam(required = true) int userId) {
        return ResponseEntity.ok(taskService.assignUserToTask(taskId, userId));
    }


}
