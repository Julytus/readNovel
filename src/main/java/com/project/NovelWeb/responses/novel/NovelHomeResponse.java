package com.project.NovelWeb.responses.novel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NovelHomeResponse {
    protected long id;
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("last_chapter")
    private String lastChapter;
}
