package com.project.NovelWeb.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.NovelWeb.models.entities.novel.ContentType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentTypeResponse extends BaseResponse{
    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<String> errors;

    @JsonProperty("content_type")
    private ContentType contentType;
}

