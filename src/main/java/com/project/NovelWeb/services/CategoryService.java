package com.project.NovelWeb.services;

import com.project.NovelWeb.models.dtos.requests.CategoryRequest;
import com.project.NovelWeb.models.Novel.Category;
import com.project.NovelWeb.models.Novel.Novel;
import com.project.NovelWeb.repositories.CategoryRepository;
import com.project.NovelWeb.repositories.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    private final NovelRepository novelRepository;
    @Override
    public Category createCategory(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CATEGORY NOT FOUND!"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(CategoryRequest categoryRequest,
                                   Long id) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(categoryRequest.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public Category deleteCategory(Long id) throws ChangeSetPersister.NotFoundException {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        List<Novel> novels = novelRepository.findByCategory(existingCategory);
        if (!novels.isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated novels");
        } else {
            categoryRepository.deleteById(id);
            return existingCategory;
        }
    }
}
