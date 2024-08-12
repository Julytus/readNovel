package com.project.NovelWeb.controllers;

import com.project.NovelWeb.models.dtos.novel.ContentTypeDTO;
import com.project.NovelWeb.models.entities.novel.ContentType;
import com.project.NovelWeb.responses.ContentTypeResponse;
import com.project.NovelWeb.responses.ResponseObject;
import com.project.NovelWeb.services.impl.ContentTypeServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/contentTypes")
public class ContentTypeController {
    private final ContentTypeServiceImp contentTypeServiceImp;

    @PostMapping("")
    public ResponseEntity<ContentTypeResponse> createContentType(
            @Valid @RequestBody ContentTypeDTO contentTypeDTO) {
        ContentTypeResponse contentTypeResponse = new ContentTypeResponse();

        ContentType contentType = contentTypeServiceImp.createContentType(contentTypeDTO);
        contentTypeResponse.setContentType(contentType);
        return ResponseEntity.ok(contentTypeResponse);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllContentType(
            @RequestParam("page")   int page,
            @RequestParam("limit")   int limit) {
        List<ContentType> categoryList = contentTypeServiceImp.getAllContentTypes();
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Get list of Categories successfully.")
                        .data(categoryList)
                        .status(HttpStatus.OK)
                        .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getContentTypeById(
            @PathVariable("id") Long id) {
    ContentType category = contentTypeServiceImp.getContentTypeById(id);
    return ResponseEntity.ok(ResponseObject.builder()
            .status(HttpStatus.OK)
            .message("Get category information successfully.")
            .data(category)
            .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateContentType(
            @PathVariable("id") Long id,
            @Valid @RequestBody ContentTypeDTO contentTypeDTO
    ) {
        contentTypeServiceImp.updateContentType(contentTypeDTO, id);
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("UPDATE_CONTENT_TYPE_SUCCESSFULLY.")
                        .data(contentTypeServiceImp.getContentTypeById(id))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteContentType(
            @PathVariable("id") long id
    ) throws ChangeSetPersister.NotFoundException {
        contentTypeServiceImp.deleteContentType(id);
        return ResponseEntity.ok(ResponseObject
                        .builder()
                        .status(HttpStatus.OK)
                        .message("DELETE_CATEGORY_SUCCESSFULLY.")
                        .build());
    }
}
