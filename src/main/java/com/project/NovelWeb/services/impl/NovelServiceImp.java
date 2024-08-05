package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.exceptions.MaximumMemoryExceededException;
import com.project.NovelWeb.mappers.NovelResponseMapper;
import com.project.NovelWeb.utils.EnumUtils;
import com.project.NovelWeb.enums.Status;
import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.models.dtos.novel.NovelDTO;
import com.project.NovelWeb.models.entities.novel.ContentType;
import com.project.NovelWeb.models.entities.novel.Novel;
import com.project.NovelWeb.models.entities.User;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NovelServiceImp implements NovelService {
    private final NovelRepository novelRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final UserRepository userRepository;
    private static final String UPLOADS_FOLDER = "uploads";
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
    public NovelResponse updateNovel(Long novelId, NovelDTO novelDTO) throws Exception {
        Novel existingNovel = getNovelById(novelId);
        if (existingNovel != null) {
            List<ContentType> contentTypes = novelDTO.getContentTypeId().stream()
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
            if (novelDTO.getName() != null && !novelDTO.getName().isEmpty()) {
                existingNovel.setName(novelDTO.getName());
            }
            if (novelDTO.getAlias() != null && !novelDTO.getAlias().isEmpty()) {
                existingNovel.setAlias(novelDTO.getAlias());
            }
            if (novelDTO.getPosterId() != null ) {
                User existingUser = userRepository
                        .findById(novelDTO.getPosterId())
                        .orElseThrow(() ->
                                new DataNotFoundException(
                                        "Cannot find User with id:" + novelDTO.getPosterId()));
                existingNovel.setPoster(existingUser);
            }
            if (novelDTO.getContent() != null && !novelDTO.getContent().isEmpty()) {
                existingNovel.setContent(novelDTO.getContent());
            }
            Status existingStatus = Status.valueOf(novelDTO.getStatus().toUpperCase());
            if (!EnumUtils.isValidEnum(Status.class, novelDTO.getStatus())) {
                throw new Exception("Invalid status value: " + novelDTO.getStatus());
            }
            existingNovel.setStatus(existingStatus);
            Novel novel = novelRepository.save(existingNovel);
            return NovelResponseMapper.fromNovel(novel);
        }
        return null;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    @Override
    public Novel updateImage(MultipartFile file, Novel novel) throws IOException{
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new MaximumMemoryExceededException("Image must be smaller than 5MB");
        }
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("invalid image format.");
        }

        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")) {
            throw new UnsupportedEncodingException("Payload must be images");
        }

        String fileName = UUID.randomUUID()+"_"+StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));
        java.nio.file.Path uploadDir = Paths.get(UPLOADS_FOLDER);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        java.nio.file.Path destination = Paths.get(uploadDir.toString(), fileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        novel.setImageUrl(fileName);
        return novelRepository.save(novel);
    }
}
