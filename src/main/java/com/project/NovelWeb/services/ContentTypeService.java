package com.project.NovelWeb.services;

import com.project.NovelWeb.models.dto.requests.ContentTypeRequest;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface ContentTypeService {
    ContentType createContentType(ContentTypeRequest contentTypeRequest);
    ContentType getContentTypeById(Long id);
    List<ContentType> getAllContentTypes();
    void updateContentType(ContentTypeRequest contentTypeRequest, Long id);
    void deleteContentType(Long id) throws ChangeSetPersister.NotFoundException;
}
