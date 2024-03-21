package org.example.collaboapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.collaboapp.dto.Authentication.UserResponseDto;
import org.example.collaboapp.model.User;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDto extends RepresentationModel<TaskResponseDto> {
    private int taskId;
    private String title;
    private String description;
    private String status;
    private int projectId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponseDto assignee;
}
