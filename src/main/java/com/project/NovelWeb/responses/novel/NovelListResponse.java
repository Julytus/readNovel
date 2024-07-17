package com.project.NovelWeb.responses.novel;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NovelListResponse {
    private List<NovelResponse> novelResponseList;
    private int totalPages;
}
