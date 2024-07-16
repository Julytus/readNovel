package com.project.NovelWeb.services;

import com.project.NovelWeb.models.dto.requests.ContentTypeRequest;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface CategoryService {
    ContentType createCategory(ContentTypeRequest contentTypeRequest);
    ContentType getCategoryById(Long id);
    List<ContentType> getAllCategories();
    ContentType updateCategory(ContentTypeRequest contentTypeRequest, Long id);
    ContentType deleteCategory(Long id) throws ChangeSetPersister.NotFoundException;
}
