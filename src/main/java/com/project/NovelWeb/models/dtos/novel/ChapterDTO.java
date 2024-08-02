package com.project.NovelWeb.models.dtos.novel;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDTO {
    @NotNull(message = "Novel_id cannot be null")
    @JsonProperty("novel_id")
    private Long novelId;

    @JsonProperty("chapter_num")
    @NotNull(message = "Chapter number cannot be null")
    private int chapterNum;

    @JsonProperty("chapter_name")
    private String chapterName;

    @JsonProperty("price")
    private Float price = (float) 0;

    @NotEmpty(message = "content cannot be empty")
    @JsonProperty("content")
    private String content;
}
