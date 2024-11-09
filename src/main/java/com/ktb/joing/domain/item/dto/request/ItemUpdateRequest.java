package com.ktb.joing.domain.item.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdateRequest {

    @Size(max = 100)
    private String title;

    @Size(max = 2500)
    private String content;
    private MediaType mediaType;
    private Category category;
    private List<EtcRequest> etcs;

    @Builder
    public ItemUpdateRequest(String title, String content, MediaType mediaType,
                             Category category, List<EtcRequest> etcs) {
        this.title = title;
        this.content = content;
        this.mediaType = mediaType;
        this.category = category;
        this.etcs = etcs;
    }
}
