package com.ktb.joing.domain.user.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "ProductManager")
public class ProductManager extends User {

    //기획자가 삭제되면 해당 선호 카테고리도 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteCategory> favoriteCategories = new ArrayList<>();

    @Builder
    public ProductManager(String username, String nickname, String email, String profileImage,
                          Boolean profileSetup, String socialId, Role role,
                          SocialProvider socialProvider) {
        super(username, nickname, email, profileImage, profileSetup, socialId, role, socialProvider);
    }

}
