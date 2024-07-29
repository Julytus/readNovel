package com.project.NovelWeb.repositories;

import com.project.NovelWeb.models.entity.Novel.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Long> {
}
