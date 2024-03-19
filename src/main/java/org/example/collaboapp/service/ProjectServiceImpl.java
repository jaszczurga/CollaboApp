package org.example.collaboapp.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.example.collaboapp.controller.ProjectController;
import org.example.collaboapp.dto.Mapper.EntityMapper;
import org.example.collaboapp.dto.ProjectRequestDto;
import org.example.collaboapp.dto.ProjectResponseDto;
import org.example.collaboapp.exception.NotFoundException;
import org.example.collaboapp.model.Project;
import org.example.collaboapp.repository.ProjectRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final EntityMapper entityMapper;

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto) {
        Project project = entityMapper.projectRequestDtoToProject( projectRequestDto );
        Project savedProject = projectRepository.save(project);
        return entityMapper.projectToProjectResponseDto(savedProject);
    }

    @Override
    public List<ProjectResponseDto> getAllProjects(int page , int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Project> projects = projectRepository.findAll(pageable).getContent();
        List<ProjectResponseDto> projectResponseDtoList = projects.stream()
                .map( entityMapper::projectToProjectResponseDto )
                .peek(projectResponseDto -> {
                    Link selfLink = linkTo(methodOn( ProjectController.class).getProject(projectResponseDto.getProjectId())).withSelfRel();
                    projectResponseDto.add(selfLink);
                })
                .toList();

        return projectResponseDtoList;
    }

    @Override
    public ProjectResponseDto getProjectById(int id) {
        Project project = projectRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException( "project not found with given id" ) );
        return entityMapper.projectToProjectResponseDto(project);
    }
}
