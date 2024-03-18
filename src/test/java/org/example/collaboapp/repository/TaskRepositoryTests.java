package org.example.collaboapp.repository;

import org.example.collaboapp.AbstractContainerBasedTest;
import org.example.collaboapp.model.Project;
import org.example.collaboapp.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTests extends AbstractContainerBasedTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Task task;
    private Project project;

    @BeforeEach
    public void setUp() {
        projectRepository.deleteAll();
        taskRepository.deleteAll();
        this.project = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .build();
        projectRepository.save(project);

        this.task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .projectId( project.getProjectId() )
                .build();
    }

    // Test for saving a task
    @Test
    public void givenTaskObject_whenSave_thenReturnSavedTask() {

        Task savedTask = taskRepository.save(task);

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getTaskId()).isGreaterThan(0);
    }

    // get all tasks operation
    @Test
    public void givenTaskList_whenFindAll_thenTaskList() {
        //given - precoditions for the test
        Task task2 = Task.builder()
                .title("Test Task2")
                .projectId( project.getProjectId() )
                .build();

        taskRepository.save(task);
        taskRepository.save(task2);

        Iterable<Task> taskList = taskRepository.findAll();

        assertThat(taskList).isNotNull();
        assertThat(taskList.spliterator().getExactSizeIfKnown()).isEqualTo(2);
    }

    // get task by id operation
    @Test
    public void givenTaskId_whenFindById_thenReturnTask() {
        //given - precoditions for the test
        Task savedTask = taskRepository.save(task);

        Task foundTask = taskRepository.findById((long) savedTask.getTaskId()).orElse(null);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getTaskId()).isEqualTo(savedTask.getTaskId());
    }

    // delete task by id operation
    @Test
    public void givenTaskId_whenDeleteById_thenTaskList() {
        //given - precoditions for the test
        Task savedTask = taskRepository.save(task);

        taskRepository.deleteById((long)savedTask.getTaskId());

        Iterable<Task> taskList = taskRepository.findAll();

        assertThat(taskList).isNotNull();
        assertThat(taskList.spliterator().getExactSizeIfKnown()).isEqualTo(0);
    }

    // update task operation
    @Test
    public void givenTaskObject_whenUpdate_thenReturnUpdatedTask() {
        //given - precoditions for the test
        Task savedTask = taskRepository.save(task);

        savedTask.setTitle("Updated Task");

        Task updatedTask = taskRepository.save(savedTask);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getTaskId()).isEqualTo(savedTask.getTaskId());
    }

    // attempt to save task with null project
    @Test
    public void givenTaskObjectWithNullProject_whenSave_thenThrowException() {
        //given - precoditions for the test
        Task taskWithNullProject = Task.builder()
                .title( "Test Task" )
                .description( "Test Description" )
                .build();
        assertThatThrownBy(() -> taskRepository.save(taskWithNullProject)).isInstanceOf(Exception.class);
    }


}
