package org.example.collaboapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.example.collaboapp.dto.Authentication.UserResponseDto;
import org.example.collaboapp.model.User;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponseDto extends RepresentationModel<ProjectResponseDto> {
    int projectId;
    String title;
    String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserResponseDto manager;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Set<UserResponseDto> users;
}
