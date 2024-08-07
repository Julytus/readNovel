package com.project.NovelWeb.controllers;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.mappers.NovelResponseMapper;
import com.project.NovelWeb.models.dtos.novel.NovelDTO;
import com.project.NovelWeb.exceptions.MethodArgumentNotValidException;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.responses.ResponseObject;
import com.project.NovelWeb.responses.novel.NovelListResponse;
import com.project.NovelWeb.responses.novel.NovelResponse;
import com.project.NovelWeb.services.NovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/novel")
public class NovelController {
    private final NovelService novelService;
    @PostMapping("")
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<NovelResponse> createNovel(
            @Valid @RequestBody NovelDTO novelDTO,
            BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(bindingResult);
        }

        NovelResponse newNovel = novelService.createNovel(novelDTO);
        return ResponseEntity.ok(newNovel);
    }

    @PostMapping("/upload/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTER')")
    public ResponseEntity<NovelResponse> updateNovelImage(
            @PathVariable("id") Long novelId,
            @ModelAttribute("file") MultipartFile file
    ) throws Exception {
        Novel existingNovel = novelService.getNovelById(novelId);
        existingNovel = novelService.updateImage(file, existingNovel);
        return ResponseEntity.ok(NovelResponseMapper.fromNovel(existingNovel));
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTER')")
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

    @GetMapping("/image/{id}")
    public ResponseEntity<?> viewNovelImage(@PathVariable Long id) {
        try {
            Novel novel = novelService.getNovelById(id);

            java.nio.file.Path imagePath = Paths.get("uploads/"+novel.getImageUrl());
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/image-not-found.jpg").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
