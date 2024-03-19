package org.example.collaboapp.dto.Mapper;

import org.example.collaboapp.dto.ProjectRequestDto;
import org.example.collaboapp.dto.ProjectResponseDto;
import org.example.collaboapp.model.Project;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    //converting from Entity to responseDto
    public ProjectResponseDto projectToProjectResponseDto(Project project) {
        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .title(project.getTitle())
                .description(project.getDescription())
                .build();
    }

    //converting from requestDto to Entity
    public Project projectRequestDtoToProject(ProjectRequestDto projectRequestDto) {
        return Project.builder()
                .title(projectRequestDto.getTitle())
                .description(projectRequestDto.getDescription())
                .build();
    }




}
