package com.ktb.joing.domain.item.entity;

import com.ktb.joing.common.model.BaseTimeEntity;
import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import com.ktb.joing.domain.user.entity.ProductManager;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2500)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column(nullable = false)
    private int score; // 디폴트 정하기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ProductManager productManager;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Etc> etcs = new ArrayList<>();

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private Summary summary;

    public void addEtc(Etc etc) {
        this.etcs.add(etc);
        etc.setItem(this);
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
        summary.setItem(this);
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
        if (productManager != null) {
            productManager.getItems().add(this);
        }
    }

    public void deleteProductManager() {
        this.productManager = null;
    }

    public void update(String title, String content, MediaType mediaType, Category category) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (mediaType != null) this.mediaType = mediaType;
        if (category != null) this.category = category;
    }

    public void updateEtcs(List<Etc> newEtcs) {
        this.etcs.clear();
        newEtcs.forEach(this::addEtc);
    }

    @Builder
    private Item(String title, String content, MediaType mediaType, int score,
                 ProductManager productManager, Category category, List<Etc> etcs, Summary summary) {
        this.title = title;
        this.content = content;
        this.mediaType = mediaType;
        this.score = score;
        this.productManager = productManager;
        this.category = category;
        this.etcs = etcs;
        this.summary = summary;
    }
}
