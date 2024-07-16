package com.project.NovelWeb.controllers;

import com.project.NovelWeb.models.dto.requests.ContentTypeRequest;
import com.project.NovelWeb.models.dto.responses.ResponseObject;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.dto.responses.ContentTypeResponse;
import com.project.NovelWeb.services.impl.ContentTypeServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/contentType")
public class ContentTypeController {
    private final ContentTypeServiceImp contentTypeServiceImp;

    @PostMapping("")
    public ResponseEntity<ContentTypeResponse> createContentType(
            @Valid @RequestBody ContentTypeRequest contentTypeRequest,
            BindingResult bindingResult) {
        ContentTypeResponse contentTypeResponse = new ContentTypeResponse();
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            contentTypeResponse.setMessage("INSERT_ContentType_FAILED");
            contentTypeResponse.setErrors(errorMessages);
            return ResponseEntity.badRequest().body(contentTypeResponse);
        }
        ContentType contentType = contentTypeServiceImp.createContentType(contentTypeRequest);
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
            @Valid @RequestBody ContentTypeRequest contentTypeRequest
    ) {
        contentTypeServiceImp.updateContentType(contentTypeRequest, id);
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("UPDATE_CATEGORY_SUCCESSFULLY.")
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
