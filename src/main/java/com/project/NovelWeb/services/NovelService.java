package com.project.NovelWeb.services;

import com.project.NovelWeb.dtos.NovelDTO;
import com.project.NovelWeb.models.entity.Novel.Novel;
import com.project.NovelWeb.responses.novel.NovelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface NovelService {
    Novel createNovel(NovelDTO novelDTO) throws Exception;

    Page<NovelResponse> getAllNovels(PageRequest pageRequest);
    Page<NovelResponse> SearchNovel(String keyword, List<Long> contentTypeId, PageRequest pageRequest);
    List<Novel> findAllByStatus(String status, Pageable pageable);
    void deleteNovel(Long id);
}
