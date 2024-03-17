package org.example.collaboapp.Service;

import org.example.collaboapp.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JwtServiceTests {

    private JwtService jwtService;

    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        jwtService = new JwtService();
    userDetails = User.withUsername("testUser")
                      .password("testPassword")
                      .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                      .build();
    }

    @Test
    public void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    public void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        assertEquals("testUser", jwtService.extractUsername(token));
    }

    @Test
    public void testIsTokenExpired() {
        String token = jwtService.generateToken(userDetails);
        assertFalse(jwtService.isTokenExpired(token));
    }
}
