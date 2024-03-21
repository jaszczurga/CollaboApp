package org.example.collaboapp.dto.Authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.collaboapp.model.Role;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    Long userId;
    String email;
    String firstName;
    String lastName;
    Set<Role> role;
}
