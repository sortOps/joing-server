package com.ktb.joing.domain.user.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "Creator")
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

    @Builder
    public Creator(String username, String nickname, String email, String profileImage,
                   Boolean profileSetup, String socialId, Role role,
                   SocialProvider socialProvider, String channelUrl,
                   Long maxViews, Long minViews, Long subscribers,
                   Long comments, MediaType mediaType, Category category) {
        super(username, nickname, email, profileImage, profileSetup, socialId, role, socialProvider);
        this.channelUrl = channelUrl;
        this.maxViews = maxViews;
        this.minViews = minViews;
        this.subscribers = subscribers;
        this.comments = comments;
        this.mediaType = mediaType;
        this.category = category;
    }

}
