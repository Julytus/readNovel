package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.mappers.ChapterResponseMapper;
import com.project.NovelWeb.models.dtos.ChapterDTO;
import com.project.NovelWeb.models.entities.Chapter;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.repositories.ChapterRepository;
import com.project.NovelWeb.repositories.NovelRepository;
import com.project.NovelWeb.responses.ChapterResponse;
import com.project.NovelWeb.services.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChapterServiceImp implements ChapterService {
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public ChapterResponse createChapter(ChapterDTO chapterDTO) throws DataNotFoundException {
        Novel novel = novelRepository.findById(chapterDTO.getNovelId())
                .orElseThrow(() -> new DataNotFoundException("Novel Not Found"));

        Chapter chapter = chapterRepository.save(Chapter.builder()
                .novel(novel)
                .chapterNum(chapterDTO.getChapterNum())
                .price(chapterDTO.getPrice())
                .content(chapterDTO.getContent())
                .name(chapterDTO.getChapterName())
                .build());

        novel.setLastChapterId(chapter.getId());
        novelRepository.save(novel);

        return ChapterResponseMapper.fromChapter(chapter, novel);
    }
}
