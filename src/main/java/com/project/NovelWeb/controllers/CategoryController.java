package com.project.NovelWeb.controllers;

import com.project.NovelWeb.models.dto.requests.CategoryRequest;
import com.project.NovelWeb.models.dto.responses.ResponseObject;
import com.project.NovelWeb.models.entity.Novel.Category;
import com.project.NovelWeb.models.dto.responses.CategoryResponse;
import com.project.NovelWeb.services.impl.CategoryServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryServiceImp categoryServiceImp;

    @PostMapping("")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest,
            BindingResult bindingResult) {
        CategoryResponse categoryResponse = new CategoryResponse();
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            categoryResponse.setMessage("INSERT_CATEGORY_FAILED");
            categoryResponse.setErrors(errorMessages);
            return ResponseEntity.badRequest().body(categoryResponse);
        }
        Category category = categoryServiceImp.createCategory(categoryRequest);
        categoryResponse.setCategory(category);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllCategories(
            @RequestParam("page")   int page,
            @RequestParam("limit")   int limit) {
        List<Category> categoryList = categoryServiceImp.getAllCategories();
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Get list of Categories successfully.")
                        .data(categoryList)
                        .status(HttpStatus.OK)
                        .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(
            @PathVariable("id") Long id) {
    Category category = categoryServiceImp.getCategoryById(id);
    return ResponseEntity.ok(ResponseObject.builder()
            .status(HttpStatus.OK)
            .message("Get category information successfully.")
            .data(category)
            .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) {
        categoryServiceImp.updateCategory(categoryRequest, id);
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("UPDATE_CATEGORY_SUCCESSFULLY.")
                        .data(categoryServiceImp.getCategoryById(id))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(
            @PathVariable("id") long id
    ) throws ChangeSetPersister.NotFoundException {
        categoryServiceImp.deleteCategory(id);
        return ResponseEntity.ok(ResponseObject
                        .builder()
                        .status(HttpStatus.OK)
                        .message("DELETE_CATEGORY_SUCCESSFULLY.")
                        .build());
    }
}
