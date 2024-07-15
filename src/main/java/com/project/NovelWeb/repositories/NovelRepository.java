package com.project.NovelWeb.repositories;

import com.project.NovelWeb.models.Novel.Category;
import com.project.NovelWeb.models.Novel.Novel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NovelRepository extends JpaRepository<Novel, Long> {
    List<Novel> findByCategory(Category category);
}
