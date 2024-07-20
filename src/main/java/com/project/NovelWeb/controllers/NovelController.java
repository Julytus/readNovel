package com.project.NovelWeb.controllers;

import com.project.NovelWeb.dtos.NovelDTO;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.entity.Novel.Novel;
import com.project.NovelWeb.responses.ResponseObject;
import com.project.NovelWeb.responses.novel.NovelListResponse;
import com.project.NovelWeb.responses.novel.NovelResponse;
import com.project.NovelWeb.services.NovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
                        .id(newNovel.getId())
                        .name(newNovel.getName())
                        .content(newNovel.getContent())
                        .image(newNovel.getImage())
                        .posterId(newNovel.getPoster().getId())
                        .message("CREATE_NOVEL_SUCCESSFULLY")
                        .contentTypeId(newNovel.getContentTypes().stream()
                        .map(ContentType::getId)
                        .collect(Collectors.toList()))
                        .build());
    }


    //show all novels
    @GetMapping("/all")
    public ResponseEntity<NovelListResponse> getAllNovels(
            @RequestParam("page")     int page,
            @RequestParam("limit")    int limit
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
        List<NovelResponse> novelResponseList = novelResponses.getContent();
        return ResponseEntity.ok(NovelListResponse.builder()
                .totalPages(novelResponses.getTotalPages())
                .novelResponseList(novelResponseList)
                .build());
    }
}
