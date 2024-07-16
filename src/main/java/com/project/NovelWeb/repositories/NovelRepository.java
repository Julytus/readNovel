package com.project.NovelWeb.repositories;

import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.entity.Novel.Novel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {
    List<Novel> findByContentType(ContentType contentType);
    @Query("SELECT n FROM Novel n WHERE " +
            "(:contentTypeId IS NULL OR EXISTS (SELECT 1 FROM n.contentTypes ct WHERE ct.id = :contentTypeId)) " +
            "AND (:keyword IS NULL OR :keyword = '' OR n.name LIKE %:keyword%)")
    Page<Novel> searchNovels(
            @Param("contentTypeId") Long contentTypeId,
            @Param("keyword") String keyword,
            Pageable pageable);
}
