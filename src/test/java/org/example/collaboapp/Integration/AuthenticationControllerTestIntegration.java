package org.example.collaboapp.Integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.collaboapp.AbstractContainerBasedTest;
import org.example.collaboapp.dto.Authentication.AuthenticationRequest;
import org.example.collaboapp.dto.Authentication.RegisterRequest;
import org.example.collaboapp.model.User;
import org.example.collaboapp.repository.UserRepository;
import org.example.collaboapp.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTestIntegration extends AbstractContainerBasedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;

    private User user;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        registerRequest = RegisterRequest.builder()
                .firstName( "Test" )
                .lastName( "Test" )
                .email("user@test.com")
                .password("password")
                .build();

        authenticationRequest = AuthenticationRequest.builder()
                .email("user@test.com")
                .password("password")
                .build();

        user = User.builder()
                .firstName( "Test" )
                .lastName( "Test" )
                .email("user@test.com")
                .password("password")
                .roles( Set.of() )
                .build();

    }

    @Test
    @DisplayName( "Register attempt valid credentials return token")
    public void givenRegisterRequestObject_whenRegisterAttempt_thenReturnTokenAndOkStatus() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)));

        response.andDo( MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    @DisplayName( "Authenticate attempt with invalid credentials")
    public void givenAuthenticationRequestBadPassword_whenAuthenticate_thenReturnForbidden() throws Exception {
        authenticationRequest.setPassword( "bad password" );

        ResultActions response = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType( MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)));
        response.andDo( MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", notNullValue()));
    }

    //authentication attempt with valid credentials
        @Test
        @DisplayName( "Authenticate attempt with valid credentials")
        public void givenAuthenticationRequest_whenAuthenticate_thenReturnToken() throws Exception{


       var user = User.builder()
        .firstName(registerRequest.getFirstName())
        .lastName(registerRequest.getLastName())
        .email(registerRequest.getEmail())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .roles(new HashSet<>( List.of()))
        .build();
            userRepository.save( user);
            //when - action or the behavior to be tested
            ResultActions response = mockMvc.perform(post("/api/v1/auth/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authenticationRequest)));

            //then - the expected result
            response.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token", notNullValue()));
        }
}
