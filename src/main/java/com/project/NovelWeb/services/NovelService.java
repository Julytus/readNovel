package com.project.NovelWeb.services;

import com.project.NovelWeb.models.dto.requests.NovelRequest;
import com.project.NovelWeb.models.entity.Novel.Novel;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface NovelService {
    Novel createNovel(NovelRequest novelRequest) throws Exception;
    List<Novel> getAllNovels();
    Novel findByName(String name);
    List<Novel> getNovels(Pageable pageable);
    List<Novel> findAllByStatus(String status, Pageable pageable);
    List<Novel> searchByName(String name, Pageable pageable);
    List<Novel> searchByNameAndType(String type, String name, Pageable pageable);
    List<Novel> searchByType(String type, Pageable pageable);
    Novel findById(Long id);
    void deleteNovel(Long id);
}
