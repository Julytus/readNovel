package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.enums.Status;
import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.mappers.NovelResponseMapper;
import com.project.NovelWeb.models.dtos.novel.NovelDTO;
import com.project.NovelWeb.models.dtos.novel.UpdateNovelDTO;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.models.entities.novel.ContentType;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.repositories.ContentTypeRepository;
import com.project.NovelWeb.repositories.NovelRepository;
import com.project.NovelWeb.repositories.UserRepository;
import com.project.NovelWeb.responses.novel.NovelResponse;
import com.project.NovelWeb.services.NovelService;
import com.project.NovelWeb.utils.EnumUtils;
import com.project.NovelWeb.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NovelServiceImp implements NovelService {
    private final NovelRepository novelRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final UserRepository userRepository;
    private static final String UPLOADS_FOLDER = "uploads/novel_images";
    @Override
    public Novel getNovelById(Long id) throws DataNotFoundException {
        Optional<Novel> optionalNovel = novelRepository.getDetailNovel(id);
        if (optionalNovel.isPresent()) {
            return optionalNovel.get();
        }
        throw new DataNotFoundException("CANNOT_FIND_NOVEL_WITH_ID: " + id);
    }

    @Override
    @Transactional
    public NovelResponse createNovel(NovelDTO novelDTO) throws Exception{

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
                .status(Status.valueOf(status.toUpperCase()))
                .contentTypes(contentTypes)
                .poster(existingUser)
                .build();
        novelRepository.save(newNovel);

        return NovelResponseMapper.fromNovel(newNovel);
    }

    @Override
    public Page<NovelResponse> getAllNovels(PageRequest pageRequest) {
        Page<Novel> novelPage = novelRepository.findAll(pageRequest);
        return novelPage.map(NovelResponseMapper::fromNovel);
    }

    @Override
    public Page<NovelResponse> SearchNovel(String keyword,
                                           List<Long> contentTypeId,
                                           PageRequest pageRequest)
    {
        int contentTypeCount = (contentTypeId == null) ? 0 : contentTypeId.size();
        if (contentTypeCount == 0) {
            Page<Novel> novelPage = novelRepository.searchNovels(null, keyword,contentTypeCount , pageRequest);
            return novelPage.map(NovelResponseMapper::fromNovel);
        }
        Page<Novel> novelPage = novelRepository.searchNovels(contentTypeId, keyword,contentTypeCount , pageRequest);
        return novelPage.map(NovelResponseMapper::fromNovel);
    }

    @Override
    public Page<NovelResponse> findAllByStatus(String status, PageRequest pageRequest) throws Exception {
        Status existingStatus = Status.valueOf(status.toUpperCase());
        if (!EnumUtils.isValidEnum(Status.class, status)) {
            throw new Exception("Invalid status value: " + status);
        }
        Page<Novel> novelPage = novelRepository.findAllByStatus(existingStatus, pageRequest);
        return novelPage.map(NovelResponseMapper::fromNovel);
    }


    @Override
    @Transactional
    public void deleteNovel(Long id) {
        Optional<Novel> optionalNovel = novelRepository.findById(id);
        optionalNovel.ifPresent(novelRepository::delete);
    }

    @Override
    @Transactional
    public NovelResponse updateNovel(Long novelId, UpdateNovelDTO updateNovelDTO) throws Exception {
        Novel existingNovel = getNovelById(novelId);
        if (!updateNovelDTO.getContentTypeId().isEmpty()) {
            List<ContentType> contentTypes = updateNovelDTO.getContentTypeId().stream()
                    .map(id -> {
                        try {
                            return contentTypeRepository.findById(id)
                                    .orElseThrow(() -> new DataNotFoundException(
                                            "Cannot find ContentType with id: " + id));
                        } catch (DataNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
            existingNovel.setContentTypes(contentTypes);
        }
            if (updateNovelDTO.getName() != null && !updateNovelDTO.getName().isEmpty()) {
                existingNovel.setName(updateNovelDTO.getName());
            }
            if (updateNovelDTO.getAlias() != null && !updateNovelDTO.getAlias().isEmpty()) {
                existingNovel.setAlias(updateNovelDTO.getAlias());
            }
            if (updateNovelDTO.getPosterId() != null ) {
                User existingUser = userRepository
                        .findById(updateNovelDTO.getPosterId())
                        .orElseThrow(() ->
                                new DataNotFoundException(
                                        "Cannot find User with id:" + updateNovelDTO.getPosterId()));
                existingNovel.setPoster(existingUser);
            }
            if (updateNovelDTO.getContent() != null && !updateNovelDTO.getContent().isEmpty()) {
                existingNovel.setContent(updateNovelDTO.getContent());
            }
            if (updateNovelDTO.getStatus() != null) {
                Status existingStatus = Status.valueOf(updateNovelDTO.getStatus().toUpperCase());
                if (!EnumUtils.isValidEnum(Status.class, updateNovelDTO.getStatus())) {
                    throw new Exception("Invalid status value: " + updateNovelDTO.getStatus());
                }
                existingNovel.setStatus(existingStatus);
            }
            Novel novel = novelRepository.save(existingNovel);
            return NovelResponseMapper.fromNovel(novel);
    }

    @Override
    public Novel updateImage(MultipartFile file, Novel novel) throws IOException{
        FileUtils.updateImage(novel, file, UPLOADS_FOLDER, novel.getId());
        return novelRepository.save(novel);
    }
}
