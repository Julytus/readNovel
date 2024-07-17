package com.project.NovelWeb.services;

import com.project.NovelWeb.dtos.NovelDTO;
import com.project.NovelWeb.models.entity.Novel.Novel;
import com.project.NovelWeb.responses.NovelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface NovelService {
    Novel createNovel(NovelDTO novelDTO) throws Exception;
    Page<NovelResponse> getAllNovels(String keyword, Long contentTypeId, PageRequest pageRequest);
    Novel findByName(String name);
    List<Novel> getNovels(Pageable pageable);
    List<Novel> findAllByStatus(String status, Pageable pageable);
    List<Novel> searchByName(String name, Pageable pageable);
    List<Novel> searchByNameAndType(String type, String name, Pageable pageable);
    List<Novel> searchByType(String type, Pageable pageable);
    Novel findById(Long id);
    void deleteNovel(Long id);
}
