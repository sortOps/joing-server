package com.ktb.joing.domain.auth.service;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.auth.dto.KakaoResponse;
import com.ktb.joing.domain.auth.dto.OAuth2Response;
import com.ktb.joing.domain.auth.dto.UserDto;
import com.ktb.joing.domain.user.entity.ProductManager;
import com.ktb.joing.domain.user.entity.Role;
import com.ktb.joing.domain.user.entity.SocialProvider;
import com.ktb.joing.domain.user.entity.User;
import com.ktb.joing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2UserRequest={}", oAuth2User);

        // 응답 데이터 받기
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        User existData = userRepository.findByUsername(username).orElse(null);

        if (existData == null) {
            User user = ProductManager.builder() // ProductManager로 임시 저장(회원가입 로직 후 변경 예정)
                    .username(username)
                    .email(oAuth2Response.getEmail())
                    .nickname(oAuth2Response.getName())
                    .profileImage(oAuth2Response.getProfileImage())
                    .profileSetup(false)
                    .role(Role.ROLE_USER)
                    .socialId(oAuth2Response.getProviderId())
                    .socialProvider(SocialProvider.KAKAO)
                    .build();

            userRepository.save(user);

            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            userDto.setName(oAuth2Response.getName());
            userDto.setRole(Role.ROLE_USER);

            return new CustomOAuth2User(userDto);
        }
        else {
            existData.updateEmail(oAuth2Response.getEmail());
            existData.updateNickname(oAuth2Response.getName());
            existData.updateProfileImage(oAuth2Response.getProfileImage());

            userRepository.save(existData);

            UserDto userDto = new UserDto();
            userDto.setUsername(existData.getUsername());
            userDto.setName(oAuth2Response.getName());
            userDto.setRole(existData.getRole());

            return new CustomOAuth2User(userDto);
        }
    }
}
