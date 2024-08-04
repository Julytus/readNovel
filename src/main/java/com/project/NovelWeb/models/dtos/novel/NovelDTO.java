package com.project.NovelWeb.models.dtos.novel;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

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
    @JsonProperty("content_type_id")
    private List<Long> contentTypeId;
    @JsonProperty("poster_id")
    private Long posterId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("author_id")
    private Long authorId;
}
