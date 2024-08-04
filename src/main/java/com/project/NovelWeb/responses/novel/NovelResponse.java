package com.project.NovelWeb.responses.novel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.NovelWeb.responses.BaseResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NovelResponse extends BaseResponse {
    protected Long id;
    private String name;
    private String content;
    private String status;
    private String image;
    @JsonProperty("poster_id")
    private Long posterId;
    @JsonProperty("author_id")
    private Long authorId;
    private String message;
    private List<String> errors;
    @JsonProperty("content_type_id")
    private List<Long> contentTypeId;
    @JsonProperty("last_chapter_id")
    private Integer lastChapterId;
}
