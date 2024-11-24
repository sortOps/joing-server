package com.ktb.joing.domain.user.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorSignupRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "올바른 이메일 형식이 아닙니다")
    private String email;

    private String channelId;
    private String channelUrl;
    private MediaType mediaType;
    private Category category;

    @Builder
    public CreatorSignupRequest(String nickname, String email, String channelId, String channelUrl, MediaType mediaType, Category category) {
        this.nickname = nickname;
        this.email = email;
        this.channelId = channelId;
        this.channelUrl = channelUrl;
        this.mediaType = mediaType;
        this.category = category;
    }

}
