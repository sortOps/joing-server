package com.ktb.joing.domain.user.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateRequest {
    @Size(max = 20, message = "닉네임은 최대 20자까지 가능합니다")
    private String nickname;

    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "올바른 이메일 형식이 아닙니다")
    private String email;

    private MediaType mediaType;
    private Category category;
    private String channelUrl;
    private List<Category> favoriteCategories;

    @Builder
    public UserUpdateRequest(String nickname, String email, MediaType mediaType,
                             Category category, String channelUrl, List<Category> favoriteCategories) {
        this.nickname = nickname;
        this.email = email;
        this.mediaType = mediaType;
        this.category = category;
        this.channelUrl = channelUrl;
        this.favoriteCategories = favoriteCategories;
    }
}
