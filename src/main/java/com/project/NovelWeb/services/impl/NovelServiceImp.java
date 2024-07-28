package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.enums.EnumUtils;
import com.project.NovelWeb.enums.Status;
import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.dtos.NovelDTO;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.entity.Novel.Novel;
import com.project.NovelWeb.models.entity.User;
import com.project.NovelWeb.repositories.ContentTypeRepository;
import com.project.NovelWeb.repositories.NovelRepository;
import com.project.NovelWeb.repositories.UserRepository;
import com.project.NovelWeb.responses.novel.NovelResponse;
import com.project.NovelWeb.services.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NovelServiceImp implements NovelService {
    private final NovelRepository novelRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final UserRepository userRepository;

    @Override
    public Novel createNovel(NovelDTO novelDTO) throws Exception{

        //Get List ContentType for Novel
        List<ContentType> contentTypes = novelDTO.getContentTypeId().stream()
                .map(id -> {
                    try {
                        return contentTypeRepository.findById(id)
                                .orElseThrow(() -> new DataNotFoundException("Cannot find ContentType with id: " + id));
                    } catch (DataNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        // Check Poster

        User existingUser = userRepository
                .findById(novelDTO.getPosterId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find User with id:" + novelDTO.getPosterId()));

        String status = novelDTO.getStatus();

        if (status == null || novelDTO.getStatus().trim().isEmpty()) {
            novelDTO.setStatus("ONGOING");
        }

        if (!EnumUtils.isValidEnum(Status.class, status)) {
            throw new Exception("Invalid status value: " + status);
        }

        Novel newNovel = Novel
                .builder()
                .name(novelDTO.getName())
                .alias(novelDTO.getAlias())
                .content(novelDTO.getContent())
                .image(novelDTO.getImage())
                .status(Status.valueOf(status.toUpperCase()))
                .contentTypes(contentTypes)
                .poster(existingUser)
                .build();
        novelRepository.save(newNovel);
        return newNovel;
    }

    @Override
    public Page<NovelResponse> getAllNovels(PageRequest pageRequest) {
        Page<Novel> novelPage = novelRepository.findAll(pageRequest);
        return novelPage.map(NovelResponse::fromNovel);
    }

    @Override
    public Page<NovelResponse> SearchNovel(String keyword,
                                            List<Long> contentTypeId,
                                            PageRequest pageRequest)
    {
        int contentTypeCount = contentTypeId == null ? 0 : contentTypeId.size();
        Page<Novel> novelPage = novelRepository.searchNovels(contentTypeId, keyword,contentTypeCount , pageRequest);
        return novelPage.map(NovelResponse::fromNovel);
    }

    @Override
    public Page<NovelResponse> findAllByStatus(String status, PageRequest pageRequest) throws Exception {
        Status existingStatus = Status.valueOf(status.toUpperCase());
        if (!EnumUtils.isValidEnum(Status.class, status)) {
            throw new Exception("Invalid status value: " + existingStatus);
        }
        Page<Novel> novelPage = novelRepository.findAllByStatus(existingStatus, pageRequest);
        return novelPage.map(NovelResponse::fromNovel);
    }


    @Override
    @Transactional
    public void deleteNovel(Long id) {
        Optional<Novel> optionalNovel = novelRepository.findById(id);
        optionalNovel.ifPresent(novelRepository::delete);
    }
}
