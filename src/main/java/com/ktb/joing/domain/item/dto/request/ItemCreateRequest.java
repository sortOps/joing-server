package com.ktb.joing.domain.item.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCreateRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(max = 2500)
    private String content;

    @NotNull(message = "미디어 타입은 필수입니다")
    private MediaType mediaType;

    @NotNull(message = "카테고리는 필수입니다")
    private Category category;

    private List<EtcRequest> etcs;

    @Builder
    public ItemCreateRequest(String title, String content, MediaType mediaType, Category category, List<EtcRequest> etcs) {
        this.title = title;
        this.content = content;
        this.mediaType = mediaType;
        this.category = category;
        this.etcs = etcs;
    }
}
