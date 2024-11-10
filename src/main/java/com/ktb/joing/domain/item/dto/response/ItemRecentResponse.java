package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.entity.Summary;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRecentResponse {
    private Long id;
    private String title;
    private Summary summary;

    @Builder
    public ItemRecentResponse(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.summary = item.getSummary();
    }
}
