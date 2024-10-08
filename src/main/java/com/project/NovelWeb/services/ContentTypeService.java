package com.project.NovelWeb.services;

import com.project.NovelWeb.models.dtos.novel.ContentTypeDTO;
import com.project.NovelWeb.models.entities.novel.ContentType;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface ContentTypeService {
    ContentType createContentType(ContentTypeDTO contentTypeDTO);
    ContentType getContentTypeById(Long id);
    List<ContentType> getAllContentTypes();
    void updateContentType(ContentTypeDTO contentTypeDTO, Long id);
    void deleteContentType(Long id) throws ChangeSetPersister.NotFoundException;

}
