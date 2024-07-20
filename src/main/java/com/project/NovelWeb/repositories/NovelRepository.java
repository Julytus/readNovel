package com.project.NovelWeb.repositories;

import com.project.NovelWeb.models.entity.Novel.Novel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {
    //chỉ tìm được novel có 1 contentType
//    @Query("SELECT n FROM Novel n WHERE " +
//            "(:contentTypeId IS NULL OR :contentTypeId = '' " +
//            "OR EXISTS (SELECT 1 FROM n.contentTypes ct WHERE ct.id = :contentTypeId)) " +
//            "AND (:keyword IS NULL OR :keyword = '' OR n.name LIKE %:keyword%)")

    @Query("SELECT DISTINCT n FROM Novel n " +
            "JOIN n.contentTypes ct " +
            "WHERE (:contentTypeId IS NULL OR ct.id IN :contentTypeId) " +
            "AND (:keyword IS NULL OR :keyword = '' OR n.name LIKE %:keyword%)" +
            "GROUP BY n.id " +
            "HAVING COUNT(DISTINCT ct.id) = :contentTypeCount")

    Page<Novel> searchNovels(
            @Param("contentTypeId") List<Long> contentTypeId,
            @Param("keyword") String keyword,
            @Param("contentTypeCount") int contentTypeCount,
            Pageable pageable);
}
