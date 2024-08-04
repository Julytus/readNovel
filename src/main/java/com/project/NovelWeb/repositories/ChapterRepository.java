package com.project.NovelWeb.repositories;

import com.project.NovelWeb.models.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

    @Query("SELECT nc.id FROM Chapter nc WHERE nc.novel.id = :novelId ORDER BY nc.chapterNum DESC LIMIT 1")
    Integer findLastChapterIdByNovelId(@Param("novelId") Long novelId);
}