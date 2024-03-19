package org.example.collaboapp.service;

import org.example.collaboapp.dto.ProjectRequestDto;
import org.example.collaboapp.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto);

    List<ProjectResponseDto> getAllProjects(int page, int size);

    ProjectResponseDto getProjectById(int id);
}
