package com.project.NovelWeb.mappers;

import com.project.NovelWeb.models.entities.novel.ContentType;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.responses.novel.NovelResponse;

import java.util.List;
import java.util.stream.Collectors;

public class NovelResponseMapper {
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
                .lastChapterId(novel.getLastChapterId())
                .contentTypeId(novel.getContentTypes().stream()
                        .map(ContentType::getId)
                        .collect(Collectors.toList()))
                .message("SUCCESSFULLY")
                .build();
    }

//    public static List<NovelResponse> fromNovelList(List<Novel> novels) {
//        return novels.stream()
//                .map(NovelResponseMapper::fromNovel)
//                .collect(Collectors.toList());
//    }
}
