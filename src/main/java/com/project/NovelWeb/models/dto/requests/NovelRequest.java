package com.project.NovelWeb.models.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovelRequest {
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
    private Long contentTypeId;
    @JsonProperty("poster_id")
    private Long posterId;
}