package com.project.NovelWeb.responses.novel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.NovelWeb.models.entities.novel.ContentType;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.responses.BaseResponse;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NovelResponse extends BaseResponse {
    protected long id;
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

    public static NovelResponse fromNovel(Novel novel) {
        return NovelResponse
                .builder()
                .id(novel.getId())
                .name(novel.getName())
                .content(novel.getContent())
                .status(novel.getStatus().toString())
                .image(novel.getImageUrl())
                .posterId(novel.getPoster().getId())
                .authorId(novel.getAuthorId())
                .contentTypeId(novel.getContentTypes().stream()
                        .map(ContentType::getId)
                        .collect(Collectors.toList()))
                .build();
    }

    public static List<NovelResponse> fromNovelList(List<Novel> novels) {
        return novels.stream()
                .map(NovelResponse::fromNovel)
                .collect(Collectors.toList());
    }
}
