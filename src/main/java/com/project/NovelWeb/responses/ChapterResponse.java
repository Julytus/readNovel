package com.project.NovelWeb.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.NovelWeb.responses.BaseResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChapterResponse extends BaseResponse {
    @JsonProperty("novel_name")
    private String novelName;
    @JsonProperty("chapter_num")
    private int chapterNum;
    @JsonProperty("chapter_name")
    private String chapterName;
    private Float price;
    private String content;
}
