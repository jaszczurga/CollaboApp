package org.example.collaboapp.service;

import org.example.collaboapp.dto.ProjectRequestDto;
import org.example.collaboapp.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto createProject(ProjectRequestDto projectRequestDto);

    ProjectResponseDto updateProject(int id, ProjectRequestDto projectRequestDto);

    ProjectResponseDto deleteProject(int id);

    List<ProjectResponseDto> getAllProjects(int page, int size);

    ProjectResponseDto getProjectById(int id);

    ProjectResponseDto assignUserToProject(int id, int userId);


}
