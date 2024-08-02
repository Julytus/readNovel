package com.project.NovelWeb.repositories;

import com.project.NovelWeb.models.entities.Novel.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Long> {
}
