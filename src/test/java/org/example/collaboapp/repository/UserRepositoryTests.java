package org.example.collaboapp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.collaboapp.AbstractContainerBasedTest;
import org.example.collaboapp.model.Role;
import org.example.collaboapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests extends AbstractContainerBasedTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        this.user = User.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@test.com")
                .password("password")
                .build();
    }
    //JUnit test for testing save the user
    @DisplayName("Test for saving a user")
    @Test
    public void givenUserObject_whenSave_thenReturnSavedUser() {

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isGreaterThan(0);
    }

    //JUnit test for testing find all users
    @DisplayName("get all users operation")
    @Test
    public void givenUserList_whenFindAll_thenUserList() {
        //given - precoditions for the test

        User user2 = User.builder()
                .firstName( "Test2" )
                .lastName( "User2" )
                .email( "test2@test.com" )
                .password( "password" )
                .build();

        userRepository.save( user );
        userRepository.save( user2 );

        List<User> userList = userRepository.findAll();

        assertThat( userList ).isNotNull();
        assertThat( userList.size() ).isEqualTo( 2 );
    }

    //JUnit test for testing delete user by id
    @DisplayName("delete user by id operation")
    @Test
    public void givenUserId_whenDeleteById_thenUserList() {

        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getUserId());

        List<User> userList = userRepository.findAll();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(0);
    }

    //JUnit test for testing update user
    @DisplayName("update user operation")
    @Test
    public void givenUserObject_whenUpdate_thenReturnUpdatedUser() {
        //given - precoditions for the test

        User savedUser = userRepository.save( user );

        savedUser.setFirstName( "Updated Test" );
        savedUser.setLastName( "Updated User" );
        savedUser.setEmail( "updated@mail.com" );

        User updatedUser = userRepository.save( savedUser );

        assertThat( updatedUser ).isNotNull();
        assertThat( updatedUser.getUserId() ).isEqualTo( savedUser.getUserId() );
        assertThat( updatedUser.getFirstName() ).isEqualTo( "Updated Test" );
        assertThat( updatedUser.getLastName() ).isEqualTo( "Updated User" );
        assertThat( updatedUser.getEmail() ).isEqualTo( "updated@mail.com" );
    }

    //JUnit test for testing find user by email
    @DisplayName("get user by email operation")
    @Test
    public void givenUserEmail_whenFindByEmail_thenReturnUser() {
        //given - precoditions for the test

        User savedUser = userRepository.save( user );

        User foundUser = userRepository.findByEmail( savedUser.getEmail() ).get();

        assertThat( foundUser ).isNotNull();
        assertThat( foundUser.getUserId() ).isEqualTo( savedUser.getUserId() );
    }


}
