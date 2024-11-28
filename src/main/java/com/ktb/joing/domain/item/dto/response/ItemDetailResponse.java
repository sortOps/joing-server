package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDetailResponse {
    private Long id;
    private String nickname;
    private String email;
    private String title;
    private String content;
    private MediaType mediaType;
    private Category category;
    private List<EtcResponse> etcs;
    private SummaryView summary;

    @Builder
    public ItemDetailResponse(Item item) {
        this.id = item.getId();
        this.nickname = item.getProductManager().getNickname();
        this.email = item.getProductManager().getEmail();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.mediaType = item.getMediaType();
        this.category = item.getCategory();
        this.etcs = item.getEtcs().stream()
                .map(etc -> EtcResponse.builder()
                        .etc(etc)
                        .build())
                .toList();
        this.summary = new SummaryView(
                item.getSummary().getTitle(),
                item.getSummary().getContent(),
                Arrays.asList(item.getSummary().getKeyword().split(","))
        );
    }
}