package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.dtos.ContentTypeDTO;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.entity.Novel.Novel;
import com.project.NovelWeb.repositories.ContentTypeRepository;
import com.project.NovelWeb.repositories.NovelRepository;
import com.project.NovelWeb.services.ContentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentTypeServiceImp implements ContentTypeService {
    private final ContentTypeRepository contentTypeRepository;
    private final NovelRepository novelRepository;
    @Override
    public ContentType createContentType(ContentTypeDTO contentTypeDTO) {
        ContentType contentType = ContentType.builder()
                .name(contentTypeDTO.getName())
                .build();
        return contentTypeRepository.save(contentType);
    }

    @Override
    public ContentType getContentTypeById(Long id) {
        return contentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ContentType NOT FOUND!"));
    }

    @Override
    public List<ContentType> getAllContentTypes() {
        return contentTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void updateContentType(ContentTypeDTO contentTypeDTO,
                                  Long id) {
        ContentType existingContentType = getContentTypeById(id);
        existingContentType.setName(contentTypeDTO.getName());
        contentTypeRepository.save(existingContentType);
    }

    @Override
    @Transactional
    public void deleteContentType(Long id) throws ChangeSetPersister.NotFoundException {
        contentTypeRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Page<Novel> novels = novelRepository.searchNovels(id, null, null);
        if (!novels.isEmpty()) {
            throw new IllegalStateException("Cannot delete ContentType with associated novels");
        } else {
            contentTypeRepository.deleteById(id);
        }
    }
}
