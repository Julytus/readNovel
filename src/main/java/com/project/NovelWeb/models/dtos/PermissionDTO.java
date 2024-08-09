package com.project.NovelWeb.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    private Long id;
    @NotEmpty(message = "Name cannot be empty!")
    private String name;
    @NotEmpty(message = "apiPath cannot be empty!")
    @JsonProperty("api_path")
    private String apiPath;
    @NotEmpty(message = "Method cannot be empty!")
    private String method;
    @NotEmpty(message = "Module cannot be empty!")
    private String module;
    @JsonProperty("role_id")
    private List<Long> roleId;
}
