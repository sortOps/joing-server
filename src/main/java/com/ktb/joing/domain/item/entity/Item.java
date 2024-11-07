package com.ktb.joing.domain.item.entity;

import com.ktb.joing.common.model.BaseTimeEntity;
import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import com.ktb.joing.domain.user.entity.User;
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
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;


    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Etc> etcs = new ArrayList<>();

    @OneToOne(mappedBy = "item")
    private Summary summary;


    @Builder
    private Item(String title, String content, MediaType mediaType, int score,
                 User user, Category category, List<Etc> etcs, Summary summary) {
        this.title = title;
        this.content = content;
        this.mediaType = mediaType;
        this.score = score;
        this.user = user;
        this.category = category;
        this.etcs = etcs;
        this.summary = summary;
    }
}
