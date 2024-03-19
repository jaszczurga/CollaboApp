package org.example.collaboapp.Service;

import org.example.collaboapp.dto.Mapper.EntityMapper;
import org.example.collaboapp.dto.ProjectRequestDto;
import org.example.collaboapp.dto.ProjectResponseDto;
import org.example.collaboapp.exception.NotFoundException;
import org.example.collaboapp.model.Project;
import org.example.collaboapp.repository.ProjectRepository;
import org.example.collaboapp.service.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTests {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectRequestDto projectRequestDto;
    private Project project;
    private ProjectResponseDto projectResponseDto;

    @BeforeEach
    public void setUp() {
        projectRequestDto = new ProjectRequestDto();
        project = new Project();
        projectResponseDto = new ProjectResponseDto();
    }


    //test for saving a project
    @Test
    @DisplayName( "Test for saving a project")
    public void givenProductRequestDto_whenSaveProduct_thenReturnProductResponseDtoObject() {

        when(entityMapper.projectRequestDtoToProject(projectRequestDto)).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(entityMapper.projectToProjectResponseDto(project)).thenReturn(projectResponseDto);

        ProjectResponseDto result = projectService.createProject(projectRequestDto);

        assertThat(result).isNotNull();
        assertEquals(result, projectResponseDto);
    }

    //test for updating a project
    @Test
    @DisplayName( "Test for updating a project")
    public void givenProjectIdAndProjectRequestDto_whenUpdateProject_thenReturnUpdatedProjectResponseDtoObject() {

        when(projectRepository.findById(anyLong())).thenReturn(java.util.Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(entityMapper.projectToProjectResponseDto(project)).thenReturn(projectResponseDto);

        ProjectResponseDto result = projectService.updateProject(1, projectRequestDto);

        assertThat(result).isNotNull();
        assertEquals(result, projectResponseDto);
    }

    //test for deleting a project
    @Test
    @DisplayName( "Test for deleting a project")
    public void givenProjectId_whenDeleteProject_thenReturnDeletedProjectResponseDtoObject() {

        when(projectRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(project));
        willDoNothing().given(projectRepository).delete(project);
        when(entityMapper.projectToProjectResponseDto(project)).thenReturn(projectResponseDto);

        ProjectResponseDto result = projectService.deleteProject(1);

        assertThat(result).isNotNull();
        assertEquals(result, projectResponseDto);
    }

    //test for getting all projects
    @Test
    @DisplayName("Test for getting all projects")
    public void givenPageAndSize_whenGetAllProjects_thenReturnListOfProjectResponseDtoObjects() {

        when(projectRepository.findAll(any( Pageable.class))).thenReturn(new PageImpl<>( List.of(project)));
        when(entityMapper.projectToProjectResponseDto(project)).thenReturn(projectResponseDto);

        List<ProjectResponseDto> result = projectService.getAllProjects(0, 10);

        assertThat(result).isNotNull();
        assertEquals(result, List.of(projectResponseDto));
    }

    //test for getting project by id
    @Test
    @DisplayName("Test for getting project by id")
    public void givenProjectId_whenGetProjectById_thenReturnProjectResponseDtoObject() {

        when(projectRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(project));
        when(entityMapper.projectToProjectResponseDto(project)).thenReturn(projectResponseDto);

        ProjectResponseDto result = projectService.getProjectById(1);

        assertThat(result).isNotNull();
        assertEquals(result, projectResponseDto);
    }

    //test for getting project by id with wrong id
    @Test
    @DisplayName("Test for getting project by id with wrong id")
    public void givenWrongProjectId_whenGetProjectById_thenThrowNotFoundException() {
        when(projectRepository.findById(any(Long.class))).thenReturn(java.util.Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> projectService.getProjectById(1));
    }

    //test for deleting project with wrong id
    @Test
    @DisplayName("Test for deleting project with wrong id")
    public void givenWrongProjectId_whenDeleteProject_thenThrowNotFoundException() {
        when(projectRepository.findById(any(Long.class))).thenReturn(java.util.Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> projectService.deleteProject(1));
    }

    //test for updating project with wrong id
    @Test
    @DisplayName("Test for updating project with wrong id")
    public void givenWrongProjectId_whenUpdateProject_thenThrowNotFoundException() {
        when(projectRepository.findById(any(Long.class))).thenReturn(java.util.Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> projectService.updateProject(1, projectRequestDto));
    }



}
