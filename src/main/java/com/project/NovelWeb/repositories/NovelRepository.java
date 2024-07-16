package com.project.NovelWeb.repositories;

import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.entity.Novel.Novel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {
    List<Novel> findByContentType(ContentType contentType);
}
