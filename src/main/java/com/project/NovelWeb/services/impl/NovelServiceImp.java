package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.dtos.NovelDTO;
import com.project.NovelWeb.models.entity.Novel.ContentType;
import com.project.NovelWeb.models.entity.Novel.Novel;
import com.project.NovelWeb.models.entity.User;
import com.project.NovelWeb.repositories.ContentTypeRepository;
import com.project.NovelWeb.repositories.NovelRepository;
import com.project.NovelWeb.repositories.UserRepository;
import com.project.NovelWeb.services.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class NovelServiceImp implements NovelService {
    private final NovelRepository novelRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Novel createNovel(NovelDTO novelDTO) throws Exception{
        ContentType existingContentType = contentTypeRepository
                .findById(novelDTO.getContentTypeId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find ContentType with id:" + novelDTO.getContentTypeId()));

        User existingUser = userRepository
                .findById(novelDTO.getPosterId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find User with id:" + novelDTO.getPosterId()));
        Novel newNovel = Novel
                .builder()
                .name(novelDTO.getName())
                .alias(novelDTO.getAlias())
                .content(novelDTO.getContent())
                .image(novelDTO.getImage())
                .contentType(existingContentType)
                .poster(existingUser)
                .build();
        return novelRepository.save(newNovel);
    }

    @Override
    public List<Novel> getAllNovels() {
        return novelRepository.findAll();
    }

    @Override
    public Novel findByName(String name) {
        return null;
    }

    @Override
    public List<Novel> getNovels(Pageable pageable) {
        return null;
    }

    @Override
    public List<Novel> findAllByStatus(String status, Pageable pageable) {
        return null;
    }

    @Override
    public List<Novel> searchByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public List<Novel> searchByNameAndType(String type, String name, Pageable pageable) {
        return null;
    }

    @Override
    public List<Novel> searchByType(String type, Pageable pageable) {
        return null;
    }

    @Override
    public Novel findById(Long id) {
        return null;
    }

    @Override
    public void deleteNovel(Long id) {

    }
}
