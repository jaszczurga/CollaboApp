package org.example.collaboapp.configuration;

import lombok.RequiredArgsConstructor;
import org.example.collaboapp.model.Role;
import org.example.collaboapp.model.User;
import org.example.collaboapp.repository.RoleRepository;
import org.example.collaboapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

@Bean
CommandLineRunner initDatabase(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
        if (roleRepository.findByName("USER").isEmpty()) {
            roleRepository.save(Role.builder()
                    .name("USER")
                    .description("User role")
                    .users(new HashSet<>(List.of()))
                    .build()
            );
        }
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(Role.builder()
                    .name("ADMIN")
                    .description("Admin role")
                    .users(new HashSet<>(List.of()))
                    .build()
            );
        }
        String email = "admin@admin.com";
       if (userRepository.findByEmail(email).isEmpty()) {
    User admin = User.builder()
            .email(email)
            .firstName("Admin")
            .lastName("Admin")
            .password(passwordEncoder.encode("admin"))
            .build();
    userRepository.save(admin); // Save the admin user before adding it to the role

    Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));
    adminRole.addUsers(admin);
    admin.setRoles(new HashSet<>(List.of(roleRepository.save(adminRole))));
}
    };
}


    @Bean
    public UserDetailsService userDetailsService() {
        //just override the loadUserByUsername method in more simple way
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder( passwordEncoder() );
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
