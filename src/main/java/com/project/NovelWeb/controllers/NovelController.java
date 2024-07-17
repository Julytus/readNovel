package com.project.NovelWeb.controllers;

import com.project.NovelWeb.dtos.NovelDTO;
import com.project.NovelWeb.models.entity.Novel.Novel;
import com.project.NovelWeb.responses.NovelResponse;
import com.project.NovelWeb.services.NovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/novel")
public class NovelController {
    private final NovelService novelService;
    @PostMapping("")
    public ResponseEntity<NovelResponse> createNovel(
            @Valid @RequestBody NovelDTO novelDTO,
            BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    NovelResponse.builder()
                            .errors(errorMessages)
                            .message("CREATE_NOVEL_FAILED")
                            .build()
            );
        }
        Novel newNovel = novelService.createNovel(novelDTO);
        return ResponseEntity.ok(NovelResponse.builder()
                .name(novelDTO.getName())
                .content(novelDTO.getContent())
                .image(novelDTO.getImage())
                .posterId(novelDTO.getPosterId())
                .message("CREATE_NOVEL_SUCCESSFULLY")
                .contentTypeId(novelDTO.getContentTypeId())
                .build());
    }
}
