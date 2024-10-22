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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FavoriteCategory> favoriteCategories = new ArrayList<>();

    @Builder
    public ProductManager(String nickname, String email, String profileImage,
                          Boolean profileSetup, String socialId, Role role,
                          SocialProvider socialProvider) {
        super(nickname, email, profileImage, profileSetup, socialId, role, socialProvider);
    }

}
