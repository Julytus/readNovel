package com.project.NovelWeb.models.dtos.novel;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ContentTypeDTO {
    @NotEmpty(message = "Name cannot be empty!")
    @JsonProperty("name")
    private String name;
    private Set<Long> novelIds;
}
