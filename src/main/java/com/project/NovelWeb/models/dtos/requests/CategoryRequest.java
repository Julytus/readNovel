package com.project.NovelWeb.models.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryRequest {
    @NotEmpty(message = "Category's name cannot be empty!")
    @JsonProperty("name")
    private String name;
}
