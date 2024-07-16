package com.project.NovelWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ContentTypeDTO {
    @NotEmpty(message = "Name cannot be empty!")
    @JsonProperty("name")
    private String name;
}
