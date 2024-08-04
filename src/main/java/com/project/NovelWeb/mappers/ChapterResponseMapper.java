package com.project.NovelWeb.mappers;

import com.project.NovelWeb.models.entities.Chapter;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.responses.ChapterResponse;

public class ChapterResponseMapper {
    public static ChapterResponse fromChapter(Chapter chapter, Novel novel) {
        return ChapterResponse.builder()
                .chapterName(chapter.getName())
                .chapterNum(chapter.getChapterNum())
                .novelName(novel.getName())
                .price(chapter.getPrice())
                .content(chapter.getContent())
                .build();
    }
}
