package com.ktb.joing.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED) // 상속 전략
@DiscriminatorColumn(name = "user_type") // 자식을 구분할 수 있는 컬럼
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String profileImage;

    @Column(nullable = false)
    private Boolean profileSetup;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialProvider socialProvider;

    // 자식 클래스에서 접근 가능한 protected 생성자
    protected User(String username, String nickname, String email, String profileImage,
                   Boolean profileSetup, String socialId, Role role,
                   SocialProvider socialProvider) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.profileSetup = profileSetup;
        this.socialId = socialId;
        this.role = role;
        this.socialProvider = socialProvider;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

