package com.project.NovelWeb.controllers;

import com.project.NovelWeb.exceptions.MethodArgumentNotValidException;
import com.project.NovelWeb.models.dtos.ChapterDTO;
import com.project.NovelWeb.responses.ChapterResponse;
import com.project.NovelWeb.services.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/chapter")
public class ChapterController {
    private final ChapterService chapterService;
    @PostMapping("")
    public ResponseEntity<ChapterResponse> createChapter(
            @Valid @RequestBody ChapterDTO chapterDTO,
            BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(bindingResult);
        }
        ChapterResponse chapterResponse = chapterService.createChapter(chapterDTO);
        return ResponseEntity.ok(chapterResponse);
    }
}
