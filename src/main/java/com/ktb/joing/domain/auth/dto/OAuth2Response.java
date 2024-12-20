package com.ktb.joing.domain.auth.dto;

import com.ktb.joing.domain.user.entity.SocialProvider;

public interface OAuth2Response {
    //제공자 (Ex. naver, google, ...)
    SocialProvider getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    //이메일
    String getEmail();
    //사용자 실명 (설정한 이름)
    String getName();
    //사용자 프로필 이미지
    String getProfileImage();
}
