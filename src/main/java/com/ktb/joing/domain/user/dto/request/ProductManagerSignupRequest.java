package com.ktb.joing.domain.user.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductManagerSignupRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotNull
    private List<Category> favoriteCategories;

    @Builder
    public ProductManagerSignupRequest(String nickname, String email, List<Category> favoriteCategories) {
        this.nickname = nickname;
        this.email = email;
        this.favoriteCategories = favoriteCategories;
    }
}
