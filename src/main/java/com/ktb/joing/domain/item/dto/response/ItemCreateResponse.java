package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCreateResponse {
    private Long id;
    private String title;
    private String content;
    private MediaType mediaType;
    private int score;
    private Category category;
    private String userName;
    private List<EtcResponse> etcs;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    @Builder
    public ItemCreateResponse(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.mediaType = item.getMediaType();
        this.score = item.getScore();
        this.category = item.getCategory();
        this.userName = item.getUser().getUsername();
        this.etcs = item.getEtcs().stream()
                .map(etc -> EtcResponse.builder()
                        .etc(etc)
                        .build())
                .toList();
        this.createdDateTime = item.getCreatedDateTime();
        this.updatedDateTime = item.getUpdatedDateTime();
    }
}