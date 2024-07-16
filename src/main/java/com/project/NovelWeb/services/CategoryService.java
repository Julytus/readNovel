package com.project.NovelWeb.services;

import com.project.NovelWeb.models.dto.requests.CategoryRequest;
import com.project.NovelWeb.models.entity.Novel.Category;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest categoryRequest);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(CategoryRequest categoryRequest, Long id);
    Category deleteCategory(Long id) throws ChangeSetPersister.NotFoundException;
}
