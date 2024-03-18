package org.example.collaboapp.repository;

import org.example.collaboapp.AbstractContainerBasedTest;
import org.example.collaboapp.model.Meeting;
import org.example.collaboapp.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MeetingRepositoryTests extends AbstractContainerBasedTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Meeting meeting;
    private Project project;

    @BeforeEach
    public void setUp() {
        this.project = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .build();
        projectRepository.save(project);

        this.meeting = Meeting.builder()
                .title("Test Meeting")
                .description("Test Description")
                .projectId( project.getProjectId() )
                .startDatetime(new Date())
                .endDatetime(new Date())
                .build();
    }

    @Test
    public void givenMeetingObject_whenSave_thenReturnSavedMeeting() {

        Meeting savedMeeting = meetingRepository.save(meeting);

        assertThat(savedMeeting).isNotNull();
        assertThat(savedMeeting.getMeetingId()).isGreaterThan(0);
    }

    @Test
    public void givenMeetingList_whenFindAll_thenMeetingList() {
        //given - precoditions for the test

        Meeting meeting2 = Meeting.builder()
                .title("Test Meeting2")
                .projectId( project.getProjectId() )
                .startDatetime(new Date())
                .endDatetime(new Date())
                .build();

        meetingRepository.save(meeting);
        meetingRepository.save(meeting2);

        Iterable<Meeting> meetingList = meetingRepository.findAll();

        assertThat(meetingList).isNotNull();
        assertThat(meetingList.spliterator().getExactSizeIfKnown()).isEqualTo(2);
    }

    @Test
    public void givenMeetingId_whenFindById_thenReturnMeeting() {
        //given - precoditions for the test

        Meeting savedMeeting = meetingRepository.save(meeting);

        Meeting foundMeeting = meetingRepository.findById((long)savedMeeting.getMeetingId()).orElse(null);

        assertThat(foundMeeting).isNotNull();
        assertThat(foundMeeting.getMeetingId()).isEqualTo(savedMeeting.getMeetingId());
    }

    @Test
    public void givenMeetingId_whenDeleteById_thenMeetingList() {
        //given - precoditions for the test

        Meeting savedMeeting = meetingRepository.save(meeting);

        meetingRepository.deleteById((long)savedMeeting.getMeetingId());

        Iterable<Meeting> meetingList = meetingRepository.findAll();

        assertThat(meetingList).isNotNull();
        assertThat(meetingList.spliterator().getExactSizeIfKnown()).isEqualTo(0);
    }

    @Test
    public void givenMeetingObject_whenUpdate_thenReturnUpdatedMeeting() {
        //given - precoditions for the test

        Meeting savedMeeting = meetingRepository.save(meeting);

        savedMeeting.setTitle("Updated Meeting");

        Meeting updatedMeeting = meetingRepository.save(savedMeeting);

        assertThat(updatedMeeting).isNotNull();
        assertThat(updatedMeeting.getMeetingId()).isEqualTo(savedMeeting.getMeetingId());
    }

    //saving attempt saving meeting with null project
    @Test
    public void givenMeetingObjectWithNullProject_whenSave_thenThrowException() {
        //given - precoditions for the test

        Meeting meetingWithNullProject = Meeting.builder()
                .title("Test Meeting")
                .description("Test Description")
                .startDatetime(new Date())
                .endDatetime(new Date())
                .build();
        assertThatThrownBy(() -> meetingRepository.save(meetingWithNullProject)).isInstanceOf(Exception.class);
    }

}
