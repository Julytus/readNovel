package com.project.NovelWeb.mappers;

import com.project.NovelWeb.models.entities.novel.ContentType;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.responses.novel.NovelResponse;

import java.util.List;
import java.util.stream.Collectors;

public class NovelResponseMapper {
    public static NovelResponse fromNovel(Novel novel) {

        // Chuyển đổi List<ContentType> thành List<String>
        List<String> contentTypeNames = novel.getContentTypes().stream()
                .map(ContentType::getName)
                .collect(Collectors.toList());

        NovelResponse novelResponse = NovelResponse
                .builder()
                .id(novel.getId())
                .name(novel.getName())
                .content(novel.getContent())
                .status(novel.getStatus().toString())
                .image(novel.getImageUrl())
                .posterId(novel.getPoster().getId())
                .authorId(novel.getAuthorId())
                .lastChapterId(novel.getLastChapterId())
                .contentTypes(contentTypeNames)
                .build();
        novelResponse.setCreatedAt(novel.getCreatedAt());
        novelResponse.setUpdatedAt(novel.getUpdatedAt());
        return novelResponse;
    }

//    public static List<NovelResponse> fromNovelList(List<Novel> novels) {
//        return novels.stream()
//                .map(NovelResponseMapper::fromNovel)
//                .collect(Collectors.toList());
//    }
}
