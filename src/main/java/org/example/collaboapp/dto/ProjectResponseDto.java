package org.example.collaboapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.example.collaboapp.model.User;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponseDto extends RepresentationModel<ProjectResponseDto> {
    int projectId;
    String title;
    String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    User manager;
}
