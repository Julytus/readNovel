package com.project.NovelWeb.controllers;

import com.project.NovelWeb.mappers.NovelResponseMapper;
import com.project.NovelWeb.models.dtos.novel.NovelDTO;
import com.project.NovelWeb.models.dtos.novel.UpdateNovelDTO;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.responses.ResponseObject;
import com.project.NovelWeb.responses.novel.NovelListResponse;
import com.project.NovelWeb.responses.novel.NovelResponse;
import com.project.NovelWeb.services.NovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/novels")
public class NovelController {
    private final NovelService novelService;
    private static final Logger logger = LoggerFactory.getLogger(NovelController.class);
    @PostMapping("")
    @Transactional
    public ResponseEntity<NovelResponse> createNovel(
            @Valid @RequestBody NovelDTO novelDTO) throws Exception {
        NovelResponse newNovel = novelService.createNovel(novelDTO);
        return ResponseEntity.ok(newNovel);
    }

    @PostMapping("/upload/{id}")
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
            @RequestParam(defaultValue = "10")    int size
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, size,
                Sort.by("updatedAt").descending());

        Page<NovelResponse> novelResponsePage = novelService.getAllNovels(pageRequest);
        return ResponseEntity.ok(NovelListResponse.builder()
                        .totalPages(novelResponsePage.getTotalPages())
                        .novelResponseList(novelResponsePage.getContent())
                        .build());
    }

    @GetMapping("")
    public ResponseEntity<NovelListResponse> searchNovel(
            @RequestParam(defaultValue = "0")       int page,
            @RequestParam(defaultValue = "10")      int size,
            @RequestParam(defaultValue = "")        String name,
            @RequestParam(defaultValue = "", name = "content_type_id")       List<Long> contentTypeId
    ) {
        logger.info(String.format("keyword = %s, page = %d, limit = %d",
                name, page, size));
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<NovelResponse> novelResponses = novelService.SearchNovel(name, contentTypeId, pageRequest);
        return ResponseEntity.ok(NovelListResponse.builder()
                .totalPages(novelResponses.getTotalPages())
                .novelResponseList(novelResponses.getContent())
                .build());
    }

    @GetMapping("/{status}")
    public ResponseEntity<NovelListResponse> getNovelsByStatus(
            @PathVariable("status") String status,
            @RequestParam(defaultValue = "0")       int page,
            @RequestParam(defaultValue = "10")      int size) throws Exception {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("updatedAt").descending());
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
            @Valid @RequestBody UpdateNovelDTO updateNovelDTO
    ) throws Exception {
        NovelResponse novelResponse = novelService.updateNovel(id, updateNovelDTO);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .data(novelResponse)
                .message(String.format("Novel with id = %d updated successfully", id))
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> getNovelImage(@PathVariable Long id) {
        try {
            Novel novel = novelService.getNovelById(id);

            java.nio.file.Path imagePath = Paths.get("uploads/novel_images/"+novel.getImageUrl());
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
