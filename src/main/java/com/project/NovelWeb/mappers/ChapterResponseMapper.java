package com.project.NovelWeb.mappers;

import com.project.NovelWeb.models.entities.Chapter;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.responses.ChapterResponse;

public class ChapterResponseMapper {
    public static ChapterResponse fromChapter(Chapter chapter, Novel novel) {
        ChapterResponse chapterResponse = ChapterResponse.builder()
                .chapterName(chapter.getName())
                .chapterNum(chapter.getChapterNum())
                .novelName(novel.getName())
                .price(chapter.getPrice())
                .content(chapter.getContent())
                .build();
        chapterResponse.setCreatedAt(chapter.getCreatedAt());
        chapterResponse.setUpdatedAt(chapter.getUpdatedAt());
        return chapterResponse;
    }
}
