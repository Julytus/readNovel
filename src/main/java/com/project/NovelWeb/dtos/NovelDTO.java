package com.project.NovelWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovelDTO {
    @NotEmpty(message = "Name cannot be empty!")
    @JsonProperty("name")
    private String name;
    @JsonProperty("alias")
    private String alias;
    @JsonProperty("content")
    private String content;
    @JsonProperty("image")
    private String image;
    @JsonProperty("content_type_id")
    private List<Long> contentTypeId;
    @JsonProperty("poster_id")
    private Long posterId;
}
