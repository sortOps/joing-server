package com.ktb.joing.domain.user.dto.response;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.Creator;
import com.ktb.joing.domain.user.entity.MediaType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorResponse {
    private String nickname;
    private String email;
    private String profileImage;
    private MediaType mediaType;
    private Category category;
    private String channelUrl;
    private Long subscribers;

    @Builder
    public CreatorResponse(Creator creator) {
        this.nickname = creator.getNickname();
        this.email = creator.getEmail();
        this.profileImage = creator.getProfileImage();
        this.mediaType = creator.getMediaType();
        this.category = creator.getCategory();
        this.channelUrl = creator.getChannelUrl();
        this.subscribers = creator.getSubscribers();
    }
}
