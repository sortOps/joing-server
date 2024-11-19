package com.ktb.joing.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ProductManager productManager;

    @Enumerated(EnumType.STRING)
    private Category category;

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
        if (productManager != null) {
            productManager.getFavoriteCategories().add(this);
        }
    }

    @Builder
    public FavoriteCategory(ProductManager productManager, Category category) {
        this.productManager = productManager;
        this.category = category;
    }
}
