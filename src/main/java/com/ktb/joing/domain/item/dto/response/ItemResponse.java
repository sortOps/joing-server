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
public class ItemResponse {
    private Long id;
    private String title;
    private String content;
    private MediaType mediaType;
    private int score;
    private Category category;
    private String nickname;
    private List<EtcResponse> etcs;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    @Builder
    public ItemResponse(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.mediaType = item.getMediaType();
        this.score = item.getScore();
        this.category = item.getCategory();
        this.nickname = item.getProductManager().getNickname();
        this.etcs = item.getEtcs().stream()
                .map(etc -> EtcResponse.builder()
                        .etc(etc)
                        .build())
                .toList();
        this.createdDateTime = item.getCreatedDateTime();
        this.updatedDateTime = item.getUpdatedDateTime();
    }
}