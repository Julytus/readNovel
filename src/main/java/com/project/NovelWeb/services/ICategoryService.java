package com.project.NovelWeb.services;

import com.project.NovelWeb.dtos.CategoryDTO;
import com.project.NovelWeb.models.Novel.Category;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(CategoryDTO categoryDTO, Long id);
    Category deleteCategory(Long id) throws ChangeSetPersister.NotFoundException;
}
