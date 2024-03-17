package org.example.collaboapp.Service;

import org.example.collaboapp.dto.Authentication.AuthenticationRequest;
import org.example.collaboapp.dto.Authentication.AuthenticationResponse;
import org.example.collaboapp.dto.Authentication.RegisterRequest;
import org.example.collaboapp.model.Role;
import org.example.collaboapp.model.User;
import org.example.collaboapp.repository.RoleRepository;
import org.example.collaboapp.repository.UserRepository;
import org.example.collaboapp.service.AuthenticationService;
import org.example.collaboapp.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    public void setUp() {
        when(jwtService.generateToken(any())).thenReturn("token");
        this.user = User.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@test.com")
                .build();
    }

    //JUnit test for register method
        @Test
        @DisplayName( "Test for register method")
        public void givenRegisterRequestObject_whenRegister_thenReturnAuthenticationResponseWithToken(){
            //given - precoditions for the test
            RegisterRequest request = new RegisterRequest();
            request.setEmail("test@test.com");
            request.setPassword("password");
            Role role = Role.builder().name("USER").build();

            when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
            when( roleRepository.findByName( any(String.class) ) ).thenReturn(Optional.of(role));

            //when - action or the behavior to be tested
            AuthenticationResponse response = authenticationService.register(request);

            //then - the expected result
            verify(userRepository, times(1)).save(any(User.class));
            verify(jwtService, times(1)).generateToken(any(User.class));
            assertThat(response).isNotNull();
            assertThat(response.getToken()).isEqualTo("token");
        }

    @Test
    public void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        AuthenticationResponse response = authenticationService.authenticate(request);

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(any());
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("token");
    }
}
