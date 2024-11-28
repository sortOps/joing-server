package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemResponse {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private MediaType mediaType;
    private Category category;
    private List<EtcResponse> etcs;

    @Builder
    public ItemResponse(Item item) {
        this.id = item.getId();
        this.nickname = item.getProductManager().getNickname();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.mediaType = item.getMediaType();
        this.category = item.getCategory();
        this.etcs = item.getEtcs().stream()
                .map(etc -> EtcResponse.builder()
                        .etc(etc)
                        .build())
                .toList();
    }
}
