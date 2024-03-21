package org.example.collaboapp.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.example.collaboapp.controller.ProjectController;
import org.example.collaboapp.controller.TaskController;
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

    //all updated connected to adding user to project tasks project manager and meeting TODO
    @Override
    public ProjectResponseDto updateProject(int id , ProjectRequestDto projectRequestDto) {
        Project project = projectRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException( "project not found with given id" ) );
        project.setTitle(projectRequestDto.getTitle());
        project.setDescription(projectRequestDto.getDescription());
        Project updatedProject = projectRepository.save(project);
        return entityMapper.projectToProjectResponseDto(updatedProject);
    }

    @Override
    public ProjectResponseDto deleteProject(int id) {
        Project project = projectRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException( "project not found with given id" ) );
        projectRepository.delete(project);
        return entityMapper.projectToProjectResponseDto(project);
    }

    @Override
    public List<ProjectResponseDto> getAllProjects(int page , int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Project> projects = projectRepository.findAll(pageable).getContent();
        List<ProjectResponseDto> projectResponseDtoList = projects.stream()
                .map( entityMapper::projectToProjectResponseDto )
                .peek(projectResponseDto -> {
                    Link selfLink = linkTo(methodOn( ProjectController.class).getProject(projectResponseDto.getProjectId())).withSelfRel();
//                    Link tasksLink = linkTo(methodOn( TaskController.class).getTasks(projectResponseDto.getProjectId(),0,100)).withRel("tasks");
//                    //link for deleting project
//                    Link deleteLink = linkTo(methodOn( ProjectController.class).deleteProject(projectResponseDto.getProjectId())).withRel("delete").withTitle("Endpoint for deleting project");
//                    //link for updating project
//                    Link updateLink = linkTo(methodOn( ProjectController.class).updateProject(projectResponseDto.getProjectId(),new ProjectRequestDto())).withRel("update").withTitle("Endpoint for updating project");
                    projectResponseDto.add(selfLink);
                })
                .toList();

        return projectResponseDtoList;
    }

    @Override
    public ProjectResponseDto getProjectById(int id) {
        Project project = projectRepository.findById((long)id)
                .orElseThrow(() -> new NotFoundException( "project not found with given id" ) );
        Link selfLink = linkTo(methodOn( ProjectController.class).getProject(project.getProjectId())).withSelfRel();
        Link tasksLink = linkTo(methodOn( TaskController.class).getTasks(project.getProjectId(),0,100)).withRel("tasks");
        //link for deleting project
        Link deleteLink = linkTo(methodOn( ProjectController.class).deleteProject(project.getProjectId())).withRel("delete").withTitle("Endpoint for deleting project");
        //link for updating project
        Link updateLink = linkTo(methodOn( ProjectController.class).updateProject(project.getProjectId(),new ProjectRequestDto())).withRel("update").withTitle("Endpoint for updating project");
        return entityMapper.projectToProjectResponseDto(project).add(selfLink, tasksLink, deleteLink, updateLink);
    }
}
