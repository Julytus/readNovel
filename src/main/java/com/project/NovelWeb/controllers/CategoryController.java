package com.project.NovelWeb.controllers;

import com.project.NovelWeb.models.dto.requests.CategoryRequest;
import com.project.NovelWeb.models.entity.Novel.Category;
import com.project.NovelWeb.models.dto.responses.CategoryResponse;
import com.project.NovelWeb.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
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
        Category category = categoryService.createCategory(categoryRequest);
        categoryResponse.setCategory(category);
        return ResponseEntity.ok(categoryResponse);
    }
}
