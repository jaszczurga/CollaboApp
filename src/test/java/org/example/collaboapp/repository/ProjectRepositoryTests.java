package org.example.collaboapp.repository;

import org.example.collaboapp.AbstractContainerBasedTest;
import org.example.collaboapp.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProjectRepositoryTests extends AbstractContainerBasedTest {

    @Autowired
    private ProjectRepository projectRepository;

    private Project project;

    @BeforeEach
    public void setUp() {
        projectRepository.deleteAll();
        this.project = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .build();
    }

    @DisplayName("Test for saving a project")
    @Test
    public void givenProjectObject_whenSave_thenReturnSavedProject() {

        Project savedProject = projectRepository.save(project);

        assertThat(savedProject).isNotNull();
        assertThat(savedProject.getProjectId()).isGreaterThan(0);
    }

    @DisplayName("get all projects operation")
    @Test
    public void givenProjectList_whenFindAll_thenProjectList() {
        //given - precoditions for the test

        Project project2 = Project.builder()
                .title("Test Project2")
                .build();

        projectRepository.save(project);
        projectRepository.save(project2);

        Iterable<Project> projectList = projectRepository.findAll();

        assertThat(projectList).isNotNull();
        assertThat(projectList.spliterator().getExactSizeIfKnown()).isEqualTo(2);
    }

    @DisplayName("get project by id operation")
    @Test
    public void givenProjectId_whenFindById_thenReturnProject() {
        //given - precoditions for the test

        Project savedProject = projectRepository.save(project);

       Project foundProject = projectRepository.findById((long) savedProject.getProjectId()).orElse(null);

        assertThat(foundProject).isNotNull();
        assertThat(foundProject.getProjectId()).isEqualTo(savedProject.getProjectId());
    }

    @DisplayName("delete project by id operation")
    @Test
    public void givenProjectId_whenDeleteById_thenProjectList() {
        //given - precoditions for the test

        Project savedProject = projectRepository.save(project);

        projectRepository.deleteById((long) savedProject.getProjectId());

        Iterable<Project> projectList = projectRepository.findAll();

        assertThat(projectList).isNotNull();
        assertThat(projectList.spliterator().getExactSizeIfKnown()).isEqualTo(0);

    }

    @DisplayName("update project operation")
    @Test
    public void givenProjectObject_whenUpdate_thenReturnUpdatedProject() {
        //given - precoditions for the test

        Project savedProject = projectRepository.save(project);

        savedProject.setTitle("Updated Project");

        Project updatedProject = projectRepository.save(savedProject);

        assertThat(updatedProject).isNotNull();
        assertThat(updatedProject.getProjectId()).isEqualTo(savedProject.getProjectId());
    }

}
