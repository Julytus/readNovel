package com.project.NovelWeb.controllers;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.models.dtos.ChapterDTO;
import com.project.NovelWeb.responses.ChapterResponse;
import com.project.NovelWeb.responses.ResponseObject;
import com.project.NovelWeb.services.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/chapters")
public class ChapterController {
    private final ChapterService chapterService;
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTER')")
    public ResponseEntity<ChapterResponse> createChapter(
            @Valid @RequestBody ChapterDTO chapterDTO) throws Exception {
//        if(bindingResult.hasErrors()) {
//            throw new MethodArgumentNotValidException(bindingResult);
//        }
        ChapterResponse chapterResponse = chapterService.createChapter(chapterDTO);
        return ResponseEntity.ok(chapterResponse);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTER')")
    public ResponseEntity<ResponseObject> deleteNovel(@PathVariable Integer id)
            throws DataNotFoundException {
        chapterService.deleteChapter(id);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .data(null)
                .message(String.format("Chapter with id = %d deleted successfully", id))
                .status(HttpStatus.OK)
                .build());
    }
}
