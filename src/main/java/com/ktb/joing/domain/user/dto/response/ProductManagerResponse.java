package com.ktb.joing.domain.user.dto.response;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.FavoriteCategory;
import com.ktb.joing.domain.user.entity.ProductManager;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductManagerResponse {
    private String nickname;
    private String email;
    private String profileImage;
    private List<Category> favoriteCategories;

    @Builder
    public ProductManagerResponse(ProductManager productManager) {
        this.nickname = productManager.getNickname();
        this.email = productManager.getEmail();
        this.profileImage = productManager.getProfileImage();
        this.favoriteCategories = productManager.getFavoriteCategories().stream()
                .map(FavoriteCategory::getCategory)
                .toList();
    }

}
