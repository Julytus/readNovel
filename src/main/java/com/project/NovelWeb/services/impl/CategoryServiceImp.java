package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.models.dto.requests.ContentTypeRequest;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.entity.Novel.Novel;
import com.project.NovelWeb.repositories.ContentTypeRepository;
import com.project.NovelWeb.repositories.NovelRepository;
import com.project.NovelWeb.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private final ContentTypeRepository contentTypeRepository;
    private final NovelRepository novelRepository;
    @Override
    public ContentType createCategory(ContentTypeRequest contentTypeRequest) {
        ContentType category = ContentType.builder()
                .name(contentTypeRequest.getName())
                .build();
        return contentTypeRepository.save(category);
    }

    @Override
    public ContentType getCategoryById(Long id) {
        return contentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CATEGORY NOT FOUND!"));
    }

    @Override
    public List<ContentType> getAllCategories() {
        return contentTypeRepository.findAll();
    }

    @Override
    @Transactional
    public ContentType updateCategory(ContentTypeRequest contentTypeRequest,
                                      Long id) {
        ContentType existingCategory = getCategoryById(id);
        existingCategory.setName(contentTypeRequest.getName());
        contentTypeRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public ContentType deleteCategory(Long id) throws ChangeSetPersister.NotFoundException {
        ContentType existingCategory = contentTypeRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        List<Novel> novels = novelRepository.findByCategory(existingCategory);
        if (!novels.isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated novels");
        } else {
            contentTypeRepository.deleteById(id);
            return existingCategory;
        }
    }
}
