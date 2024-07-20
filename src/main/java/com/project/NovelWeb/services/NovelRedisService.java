package com.project.NovelWeb.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.NovelWeb.responses.novel.NovelResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface NovelRedisService {
    void clear(); //clear cached data in redis
    List<NovelResponse> searchNovel(
            String keyword,
            Long contentTypeId, PageRequest pageRequest) throws JsonProcessingException;
    void saveListNovel(List<NovelResponse> novelResponses,
                       String keyword,
                       Long contentTypeId,
                       PageRequest pageRequest) throws JsonProcessingException;
}
