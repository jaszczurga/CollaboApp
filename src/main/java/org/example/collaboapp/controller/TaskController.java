package org.example.collaboapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@AllArgsConstructor
@RestController
@RequestMapping("/api/projects/{id}/tasks")
public class TaskController {

    @GetMapping("")
    public ResponseEntity<String> getTasks(@PathVariable int id) {
        return ResponseEntity.ok("Tasks for project with id " + id);
    }

}
