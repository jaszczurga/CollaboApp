package org.example.collaboapp.Integration;

import org.example.collaboapp.AbstractContainerBasedTest;
import org.example.collaboapp.dto.Mapper.EntityMapper;
import org.example.collaboapp.dto.ProjectRequestDto;
import org.example.collaboapp.dto.ProjectResponseDto;
import org.example.collaboapp.model.Project;
import org.example.collaboapp.repository.ProjectRepository;
import org.example.collaboapp.service.ProjectService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectControllerTestIntegration extends AbstractContainerBasedTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    EntityMapper entityMapper;

    private ProjectRequestDto projectRequestDto;
    private ProjectResponseDto projectResponseDto;



    @BeforeEach
    public void setUp(){
        projectRepository.deleteAll();
        projectRequestDto = ProjectRequestDto.builder()
                .title( "Test Project" )
                .description( "Test Description" )
                .build();
        projectResponseDto = ProjectResponseDto.builder()
                .projectId( 1 )
                .title( "Test Project" )
                .description( "Test Description" )
                .build();
    }

    //test for saving a project
    @Test
    @DisplayName( "Test for saving a project")
    public void givenProjectRequestDto_whenSaveProject_thenReturnProjectResponseDtoObject() throws Exception {

        ResultActions response = mockMvc.perform(post( "/api/projectController/saveProject" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( objectMapper.writeValueAsString( projectRequestDto ) )
        );

        response.andDo( MockMvcResultHandlers.print() )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.projectId", is( 1 ) ) )
                .andExpect( jsonPath( "$.title", is( "Test Project" ) ) )
                .andExpect( jsonPath( "$.description", is( "Test Description" ) ) );
    }

    //test for updating a project
    @Test
    @DisplayName( "Test for updating a project")
    public void givenProjectIdAndProjectRequestDto_whenUpdateProject_thenReturnUpdatedProjectResponseDtoObject() throws Exception {

        ProjectRequestDto projectRequestDtoUpdated = ProjectRequestDto.builder()
                .title( "updated title" )
                .description( "updated description" )
                .build();

        //save a project
        Project project = projectRepository.save( entityMapper.projectRequestDtoToProject( projectRequestDto ) );
        ResultActions response = mockMvc.perform(put( "/api/projectController/updateProject/"+project.getProjectId() )
                .contentType( MediaType.APPLICATION_JSON )
                .content( objectMapper.writeValueAsString( projectRequestDtoUpdated ) )
        );

        response.andDo( MockMvcResultHandlers.print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.projectId", is( project.getProjectId() ) ) )
                .andExpect( jsonPath( "$.title", is( "updated title" ) ) )
                .andExpect( jsonPath( "$.description", is( "updated description" ) ) );
    }

    //test for deleting a project
    @Test
    @DisplayName( "Test for deleting a project")
    public void givenProjectId_whenDeleteProject_thenReturnDeletedProjectResponseDtoObject() throws Exception {

        //save a project
        Project project = projectRepository.save( entityMapper.projectRequestDtoToProject( projectRequestDto ) );
        ResultActions response = mockMvc.perform(delete( "/api/projectController/deleteProject/"+project.getProjectId() )
                .contentType( MediaType.APPLICATION_JSON )
                .content( objectMapper.writeValueAsString( projectRequestDto ) )
        );

        response.andDo( MockMvcResultHandlers.print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.projectId", is( project.getProjectId() ) ) )
                .andExpect( jsonPath( "$.title", is( "Test Project" ) ) )
                .andExpect( jsonPath( "$.description", is( "Test Description" ) ) );
    }

    //test for getting all projects
    @Test
    @DisplayName( "Test for getting all projects")
    public void givenListOfProjects_whenGetAllProjects_thenReturnProjectsResponse() throws Exception {
        //given - precoditions for the test
        List<Project> projects = List.of(
                Project.builder()
                        .title( "Project 1" )
                        .description( "Description 1" )
                        .build() ,
                Project.builder()
                        .title( "Project 2" )
                        .description( "Description 2" )
                        .build()
        );
        projectRepository.saveAll(projects);

        //when - action or the behavior to be tested
        ResultActions response = mockMvc.perform(get( "/api/projectController/projects" ));

        //then - the expected result
        response.andDo( MockMvcResultHandlers.print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$[0].projectId", notNullValue() ) )
                .andExpect( jsonPath( "$[0].title", is( "Project 1" ) ) )
                .andExpect( jsonPath( "$[0].description", is( "Description 1" ) ) )
                .andExpect( jsonPath( "$[1].projectId", notNullValue() ) )
                .andExpect( jsonPath( "$[1].title", is( "Project 2" ) ) )
                .andExpect( jsonPath( "$[1].description", is( "Description 2" ) ) );
    }

    //test for getting a project
    @DisplayName( "Test for getting a project")
    @Test
    public void givenProjectId_whenGetProject_thenReturnProjectResponseDto() throws Exception {
        //given
        Project project = projectRepository.save( entityMapper.projectRequestDtoToProject( projectRequestDto ) );

        //when
        ResultActions result = mockMvc.perform(get( "/api/projectController/projects/"+project.getProjectId() )
                .contentType( MediaType.APPLICATION_JSON)
                .content( objectMapper.writeValueAsString( projectRequestDto )));

        //then
        result.andDo( MockMvcResultHandlers.print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.projectId", is( project.getProjectId() ) ) )
                .andExpect( jsonPath( "$.title", is( "Test Project" ) ) )
                .andExpect( jsonPath( "$.description", is( "Test Description" ) ) );
    }


}
