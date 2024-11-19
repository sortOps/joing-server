package com.ktb.joing.domain.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Etc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "etc_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 200)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    protected void setItem(Item item) {
        this.item = item;
    }

    @Builder
    public Etc(Long id, String name, String value, Item item) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.item = item;
    }
}
