package org.example.collaboapp.controller;

import lombok.AllArgsConstructor;
import org.example.collaboapp.dto.ProjectRequestDto;
import org.example.collaboapp.dto.ProjectResponseDto;
import org.example.collaboapp.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RestController
@RequestMapping("/api/projectController")
public class ProjectController {

    private final ProjectService projectService;



    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectResponseDto> getProject(@PathVariable int id) {
        ProjectResponseDto projectResponseDto = projectService.getProjectById(id);
        return ResponseEntity.ok(projectResponseDto);
    }

    @GetMapping("/projects")
    public ResponseEntity<?> getAllProjects(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "100") int size) {
        return ResponseEntity.ok(projectService.getAllProjects(page, size));
    }

    @PostMapping("/saveProject")
    public ResponseEntity<ProjectResponseDto> saveProject(@RequestBody ProjectRequestDto projectRequestDto) {
        return ResponseEntity.ok(projectService.createProject(projectRequestDto));
    }


}
