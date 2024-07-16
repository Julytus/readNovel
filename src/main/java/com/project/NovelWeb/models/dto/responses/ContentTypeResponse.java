package com.project.NovelWeb.models.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.NovelWeb.models.entity.Novel.ContentType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentTypeResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<String> errors;

    @JsonProperty("category")
    private ContentType category;
}

