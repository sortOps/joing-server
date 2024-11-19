package com.ktb.joing.domain.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private Long id;

    private String title;

    private String content;

    private String keyword;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    protected void setItem(Item item) {
        this.item = item;
    }

    @Builder
    public Summary(Long id, String title, String content, String keyword, Item item) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.keyword = keyword;
        this.item = item;
    }
}
