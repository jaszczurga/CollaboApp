package org.example.collaboapp.service;

import lombok.RequiredArgsConstructor;
import org.example.collaboapp.dto.Authentication.AuthenticationRequest;
import org.example.collaboapp.dto.Authentication.AuthenticationResponse;
import org.example.collaboapp.dto.Authentication.RegisterRequest;
import org.example.collaboapp.exception.ConflictException;
import org.example.collaboapp.exception.NotFoundException;
import org.example.collaboapp.model.User;
import org.example.collaboapp.repository.RoleRepository;
import org.example.collaboapp.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException( "User with this email already exists" );
        }
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new NotFoundException("Role not found during registration process"));

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>( Arrays.asList(userRole)))
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return  AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException( "user not found by his email" ) );
        var jwtToken = jwtService.generateToken(user);
        return  AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}