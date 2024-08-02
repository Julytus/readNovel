package com.project.NovelWeb.controllers;

import com.project.NovelWeb.models.dtos.novel.NovelDTO;
import com.project.NovelWeb.exceptions.MethodArgumentNotValidException;
import com.project.NovelWeb.responses.ResponseObject;
import com.project.NovelWeb.responses.novel.NovelListResponse;
import com.project.NovelWeb.responses.novel.NovelResponse;
import com.project.NovelWeb.services.NovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/novel")
public class NovelController {
    private final NovelService novelService;
    @PostMapping("")
    @Transactional
    public ResponseEntity<NovelResponse> createNovel(
            @Valid @RequestBody NovelDTO novelDTO,
            BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(bindingResult);
        }

        NovelResponse newNovel = novelService.createNovel(novelDTO);
        return ResponseEntity.ok(newNovel);
    }


    //show all novels
    @GetMapping("/all")
    public ResponseEntity<NovelListResponse> getAllNovels(
            @RequestParam(defaultValue = "0")     int page,
            @RequestParam(defaultValue = "10")    int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").descending());

        Page<NovelResponse> novelResponsePage = novelService.getAllNovels(pageRequest);
        List<NovelResponse> novelResponseList = novelResponsePage.getContent();
        return ResponseEntity.ok(NovelListResponse.builder()
                        .totalPages(novelResponsePage.getTotalPages())
                        .novelResponseList(novelResponseList)
                        .build());
    }

    @GetMapping("")
    public ResponseEntity<NovelListResponse> searchNovel(
            @RequestParam(defaultValue = "0")       int page,
            @RequestParam(defaultValue = "10")      int limit,
            @RequestParam(defaultValue = "")        String keyword,
            @RequestParam(defaultValue = "", name = "content_type_id")       List<Long> contentTypeId
    ) {
//        int totalPages = 0;
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<NovelResponse> novelResponses = novelService.SearchNovel(keyword, contentTypeId, pageRequest);
        return ResponseEntity.ok(NovelListResponse.builder()
                .totalPages(novelResponses.getTotalPages())
                .novelResponseList(novelResponses.getContent())
                .build());
    }

    @GetMapping("/{status}")
    public ResponseEntity<NovelListResponse> getNovelsByStatus(
            @PathVariable("status") String status,
            @RequestParam(defaultValue = "0")       int page,
            @RequestParam(defaultValue = "10")      int limit) throws Exception {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<NovelResponse> novelResponses = novelService.findAllByStatus(status, pageRequest);
        return ResponseEntity.ok(NovelListResponse.builder()
                .totalPages(novelResponses.getTotalPages())
                .novelResponseList(novelResponses.getContent())
                .build());
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ResponseObject> deleteNovel(@PathVariable Long id) {
        novelService.deleteNovel(id);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .data(null)
                .message(String.format("Novel with id = %d deleted successfully", id))
                .status(HttpStatus.OK)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateNovel(
            @PathVariable Long id,
            @Valid @RequestBody NovelDTO novelDTO
    ) throws Exception {
        NovelResponse novelResponse = novelService.updateNovel(id, novelDTO);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .data(novelResponse)
                .message(String.format("Novel with id = %d updated successfully", id))
                .status(HttpStatus.OK)
                .build());
    }
}
