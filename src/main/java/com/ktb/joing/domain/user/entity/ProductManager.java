package com.ktb.joing.domain.user.entity;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.user.dto.request.UserUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "ProductManager")
@SuperBuilder
public class ProductManager extends User {

    //기획자가 삭제되면 해당 선호 카테고리도 삭제
    @Builder.Default
    @OneToMany(mappedBy = "productManager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteCategory> favoriteCategories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "productManager")
    private List<Item> items = new ArrayList<>();

    public void addFavoriteCategory(FavoriteCategory favoriteCategory) {
        this.favoriteCategories.add(favoriteCategory);
        favoriteCategory.setProductManager(this);
    }

    public void update(UserUpdateRequest request) {
        if (request.getNickname() != null) {
            updateNickname(request.getNickname());
        }
        if (request.getEmail() != null) {
            updateEmail(request.getEmail());
        }
        if (request.getFavoriteCategories() != null && !request.getFavoriteCategories().isEmpty()) {
            this.favoriteCategories.clear();
            request.getFavoriteCategories().forEach(category -> {
                FavoriteCategory favoriteCategory = FavoriteCategory.builder()
                        .category(category)
                        .build();
                addFavoriteCategory(favoriteCategory);
            });
        }
    }

    public void delete() {
        items.forEach(Item::deleteProductManager);
    }

}
