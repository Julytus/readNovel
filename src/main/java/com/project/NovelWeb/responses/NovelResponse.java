package com.project.NovelWeb.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.NovelWeb.dtos.NovelDTO;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.entity.Novel.Novel;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NovelResponse extends BaseResponse{
    protected long id;
    private String name;
    private String content;
    private String status;
    private String image;
    private Long posterId;
    @JsonProperty("content_type_id")
    private Set<Long> contentTypeId;

    public static NovelResponse fromNovel(Novel novel) {
        NovelResponse novelResponse = NovelResponse
                .builder()
                .id(novel.getId())
                .name(novel.getName())
                .content(novel.getContent())
                .status(novel.getStatus())
                .image(novel.getImage())
                .contentTypeId(novel.getContentTypes().stream()
                        .map(ContentType::getId)
                        .collect(Collectors.toSet()))
                .build();
        return novelResponse;
    }

    public static NovelResponse fromNovelDTO(NovelDTO novelDTO) {
        NovelResponse novelResponse = new NovelResponse();
        novelResponse.setName(novelDTO.getName());
        novelResponse.setContent(novelDTO.getContent());
        novelResponse.setImage(novelDTO.getImage());
        novelResponse.setContentTypeId(novelDTO.getContentTypeId());
        novelResponse.setPosterId(novelDTO.getPosterId());
        return novelResponse;
    }
}
