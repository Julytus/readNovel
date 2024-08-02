package com.project.NovelWeb.services;

import com.project.NovelWeb.models.dtos.NovelDTO;
import com.project.NovelWeb.models.entities.Novel.Novel;
import com.project.NovelWeb.responses.novel.NovelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.util.List;

public interface NovelService {
    NovelResponse createNovel(NovelDTO novelDTO) throws Exception;

    Page<NovelResponse> getAllNovels(PageRequest pageRequest);
    Page<NovelResponse> SearchNovel(String keyword, List<Long> contentTypeId, PageRequest pageRequest);
    Page<NovelResponse> findAllByStatus(String status, PageRequest pageRequest) throws Exception;
    void deleteNovel(Long id);

    Novel getNovelById(Long id) throws Exception;

    NovelResponse updateNovel(Long id, NovelDTO novelDTO) throws Exception;
}
