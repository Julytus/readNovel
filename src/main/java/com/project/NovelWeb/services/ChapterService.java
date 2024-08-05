package com.project.NovelWeb.services;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.models.dtos.ChapterDTO;
import com.project.NovelWeb.responses.ChapterResponse;

public interface ChapterService {
    ChapterResponse createChapter(ChapterDTO chapterDTO) throws DataNotFoundException;
    void deleteChapter(Integer chapterId) throws DataNotFoundException;
}
