package com.ktb.joing.domain.auth.redis;

import com.ktb.joing.domain.user.entity.SocialProvider;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "tempUser", timeToLive = 3600) // 1시간 후 만료
public class TempUser {
    @Id
    private final String id; // socialProvider + " " + socialId 형식
    private final String profileImage;
    private final String socialId;
    private final SocialProvider socialProvider;

    @Builder
    public TempUser(String id, String profileImage, String socialId, SocialProvider socialProvider) {
        this.id = id;
        this.profileImage = profileImage;
        this.socialId = socialId;
        this.socialProvider = socialProvider;
    }
}