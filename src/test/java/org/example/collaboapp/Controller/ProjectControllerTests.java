package org.example.collaboapp.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.collaboapp.configuration.SecurityConfiguration;
import org.example.collaboapp.controller.ProjectController;
import org.example.collaboapp.dto.ProjectRequestDto;
import org.example.collaboapp.dto.ProjectResponseDto;
import org.example.collaboapp.repository.UserRepository;
import org.example.collaboapp.service.JwtService;
import org.example.collaboapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ProjectController.class)
@ImportAutoConfiguration(classes = SecurityConfiguration.class)
public class ProjectControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserRepository userRepository; // Mock the UserRepository
    @MockBean
    private AuthenticationProvider authenticationProvider;

    private ProjectRequestDto projectRequestDto;
    private ProjectResponseDto projectResponseDto;

    @BeforeEach
    public void setUp(){
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
    @DisplayName("Test for saving a project")
    @Test
    public void givenProjectRequestDto_whenSaveProject_thenReturnProjectResponseDto() throws Exception {
        //given
        given(projectService.createProject(any(ProjectRequestDto.class))).willReturn(projectResponseDto);

        //when
        ResultActions result = mockMvc.perform(post("/api/saveProject")
                .contentType( MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)));

        //then
        result.andDo( MockMvcResultHandlers.print() )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId", is(1)))
                .andExpect(jsonPath("$.title", is("Test Project")))
                .andExpect(jsonPath("$.description", is("Test Description")));
    }

    //test for updating a project
    @DisplayName("Test for updating a project")
    @Test
    public void givenProjectIdAndProjectRequestDto_whenUpdateProject_thenReturnUpdatedProjectResponseDto() throws Exception {
        //given
        given(projectService.updateProject(1, projectRequestDto)).willReturn(projectResponseDto);

        //when
        ResultActions result = mockMvc.perform(put("/api/updateProject/1")
                .contentType( MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)));

        //then
        result.andDo( MockMvcResultHandlers.print() )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId", is(1)))
                .andExpect(jsonPath("$.title", is("Test Project")))
                .andExpect(jsonPath("$.description", is("Test Description")));
    }

    //test for deleting a project
    @DisplayName("Test for deleting a project")
    @Test
    public void givenProjectId_whenDeleteProject_thenReturnDeletedProjectResponseDto() throws Exception {
        //given
        given(projectService.deleteProject(1)).willReturn(projectResponseDto);

        //when
        ResultActions result = mockMvc.perform(delete("/api/deleteProject/1")
                .contentType( MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)));

        //then
        result.andDo( MockMvcResultHandlers.print() )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId", is(1)))
                .andExpect(jsonPath("$.title", is("Test Project")))
                .andExpect(jsonPath("$.description", is("Test Description")));
    }

    //test for getting a project
    @DisplayName("Test for getting a project")
    @Test
    public void givenProjectId_whenGetProject_thenReturnProjectResponseDto() throws Exception {
        //given
        given(projectService.getProjectById(1)).willReturn(projectResponseDto);

        //when
        ResultActions result = mockMvc.perform(get("/api/projects/1")
                .contentType( MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)));

        //then
        result.andDo( MockMvcResultHandlers.print() )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId", is(1)))
                .andExpect(jsonPath("$.title", is("Test Project")))
                .andExpect(jsonPath("$.description", is("Test Description")));
    }

    //test for getting all projects
    //TODO fix this test for new implementation of getAllProjects method
//   @DisplayName("Test for getting all projects")
//@Test
//public void givenPageAndSize_whenGetAllProjects_thenReturnListOfProjectResponseDto() throws Exception {
//    //given
//
//       //list od productResponseDto
//         List<ProjectResponseDto> projectResponseDtoList = List.of(projectResponseDto);
//    given(projectService.getAllProjects(anyInt(), anyInt())).willReturn( projectResponseDtoList);
//
//    //when
//    ResultActions result = mockMvc.perform(get("/api/projectController/projects")
//            .contentType( MediaType.APPLICATION_JSON));
//
//    //then
//    result.andDo( MockMvcResultHandlers.print() )
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$[0].projectId", is(1)))
//            .andExpect(jsonPath("$[0].title", is("Test Project")))
//            .andExpect(jsonPath("$[0].description", is("Test Description")));
//}

}
