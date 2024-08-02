package com.project.NovelWeb.services;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.models.dtos.novel.ChapterDTO;
import com.project.NovelWeb.responses.novel.ChapterResponse;

public interface ChapterService {
    ChapterResponse createChapter(ChapterDTO chapterDTO) throws DataNotFoundException;
}
