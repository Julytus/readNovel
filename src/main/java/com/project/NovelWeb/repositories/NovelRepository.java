package com.project.NovelWeb.repositories;

import com.project.NovelWeb.enums.Status;
import com.project.NovelWeb.models.entities.novel.Novel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface NovelRepository extends JpaRepository<Novel, Long>
//        , JpaSpecificationExecutor<Novel>
{
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
            "HAVING COUNT(DISTINCT ct.id) = CASE WHEN :contentTypeCount = 0 " +
            "THEN COUNT(DISTINCT ct.id) ELSE :contentTypeCount END")
    Page<Novel> searchNovels(
            @Param("contentTypeId") List<Long> contentTypeId,
            @Param("keyword") String keyword,
            @Param("contentTypeCount") int contentTypeCount,
            Pageable pageable);

    
    Page<Novel> findAllByStatus(Status status, Pageable pageable);

    @Query("SELECT n FROM Novel n LEFT JOIN FETCH n.contentTypes ct WHERE n.id = :novelId")
    Optional<Novel> getDetailNovel(@Param("novelId") Long novelId);
}
