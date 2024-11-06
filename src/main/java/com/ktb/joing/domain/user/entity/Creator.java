package com.ktb.joing.domain.user.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "Creator")
@SuperBuilder
public class Creator extends User{

    private String channelUrl;

    private Long maxViews;

    private Long minViews;

    private Long subscribers;

    private Long comments;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    private Category category;

}
