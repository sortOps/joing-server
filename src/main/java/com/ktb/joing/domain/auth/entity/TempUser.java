package com.ktb.joing.domain.auth.entity;

import com.ktb.joing.domain.user.entity.Role;
import com.ktb.joing.domain.user.entity.SocialProvider;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempUser {
    @Id
    private String id; // = username = socialProvider + " " + socialId 형식
    private String profileImage;
    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public TempUser(String id, String profileImage, String socialId, SocialProvider socialProvider, Role role) {
        this.id = id;
        this.profileImage = profileImage;
        this.socialId = socialId;
        this.socialProvider = socialProvider;
        this.role = role;
    }

}
