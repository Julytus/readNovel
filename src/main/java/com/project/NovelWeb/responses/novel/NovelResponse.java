package com.project.NovelWeb.responses.novel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.entity.Novel.Novel;
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
    private Long posterId;
    private String message;
    private List<String> errors;
    @JsonProperty("content_type_id")
    private List<Long> contentTypeId;

    public static NovelResponse fromNovel(Novel novel) {
        return NovelResponse
                .builder()
                .id(novel.getId())
                .name(novel.getName())
                .content(novel.getContent())
                .status(novel.getStatus())
                .image(novel.getImage())
                .posterId(novel.getPoster().getId())
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
