package com.ktb.joing.domain.user.service;

import com.ktb.joing.domain.auth.entity.TempUser;
import com.ktb.joing.domain.auth.repository.TempUserRepository;
import com.ktb.joing.domain.user.dto.request.CreatorSignupRequest;
import com.ktb.joing.domain.user.dto.request.ProductManagerSignupRequest;
import com.ktb.joing.domain.user.entity.*;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static com.ktb.joing.domain.user.exception.UserErrorCode.DUPLICATED_NICKNAME;
import static com.ktb.joing.domain.user.exception.UserErrorCode.TEMP_USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TempUserRepository tempUserRepository;

    private TempUser tempUser;
    private final String TEST_USERNAME = "KAKAO testSocialId"; // socialProvider + " " + socialId 형식
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 임시 유저 데이터 생성
        tempUser = TempUser.builder()
                .id(TEST_USERNAME)
                .socialId("testSocialId")
                .socialProvider(SocialProvider.KAKAO)
                .profileImage("http://profile.image")
                .role(Role.ROLE_USER)
                .build();

        tempUserRepository.save(tempUser);
    }

    @Test
    @DisplayName("크리에이터 회원가입 성공")
    void creatorSignUp_Success() {
        // Given
        CreatorSignupRequest request = CreatorSignupRequest.builder()
                .nickname("creator")
                .email("creator@test.com")
                .channelUrl("http://youtube.com/creator")
                .mediaType(MediaType.LONG_FORM)
                .category(Category.ENTERTAINMENT)
                .build();

        // When
        userService.creatorSignUp(TEST_USERNAME, request, response);

        // Then
        User savedUser = userRepository.findByUsername(TEST_USERNAME)
                .orElseThrow(() -> new AssertionError("User not found"));

        assertThat(savedUser).isInstanceOf(Creator.class);
        Creator creator = (Creator) savedUser;

        // 기본 정보 검증
        assertThat(creator.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(creator.getNickname()).isEqualTo("creator");
        assertThat(creator.getEmail()).isEqualTo("creator@test.com");
        assertThat(creator.getProfileImage()).isEqualTo(tempUser.getProfileImage());
        assertThat(creator.getProfileSetup()).isTrue();
        assertThat(creator.getRole()).isEqualTo(Role.ROLE_USER);
        assertThat(creator.getSocialId()).isEqualTo(tempUser.getSocialId());
        assertThat(creator.getSocialProvider()).isEqualTo(SocialProvider.KAKAO);

        // 크리에이터 전용 정보 검증
        assertThat(creator.getChannelUrl()).isEqualTo("http://youtube.com/creator");
        assertThat(creator.getMediaType()).isEqualTo(MediaType.LONG_FORM);
        assertThat(creator.getCategory()).isEqualTo(Category.ENTERTAINMENT);

        // 임시 유저 데이터 삭제 확인
        assertThat(tempUserRepository.findById(TEST_USERNAME)).isEmpty();
    }

    @Test
    @DisplayName("기획자 회원가입 성공")
    void productManagerSignUp_Success() {
        // Given
        ProductManagerSignupRequest request = ProductManagerSignupRequest.builder()
                .nickname("manager")
                .email("manager@test.com")
                .favoriteCategories(Arrays.asList(Category.GAME, Category.TECH))
                .build();

        // When
        userService.productManagerSignUp(TEST_USERNAME, request, response);

        // Then
        User savedUser = userRepository.findByUsername(TEST_USERNAME)
                .orElseThrow(() -> new AssertionError("User not found"));

        assertThat(savedUser).isInstanceOf(ProductManager.class);
        ProductManager manager = (ProductManager) savedUser;

        // 기본 정보 검증
        assertThat(manager.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(manager.getNickname()).isEqualTo("manager");
        assertThat(manager.getEmail()).isEqualTo("manager@test.com");
        assertThat(manager.getProfileImage()).isEqualTo(tempUser.getProfileImage());
        assertThat(manager.getProfileSetup()).isTrue();
        assertThat(manager.getRole()).isEqualTo(Role.ROLE_USER);
        assertThat(manager.getSocialId()).isEqualTo(tempUser.getSocialId());
        assertThat(manager.getSocialProvider()).isEqualTo(SocialProvider.KAKAO);

        // 선호 카테고리 검증
        assertThat(manager.getFavoriteCategories())
                .hasSize(2)
                .extracting(FavoriteCategory::getCategory)
                .containsExactlyInAnyOrder(Category.GAME, Category.TECH);

        // 임시 유저 데이터 삭제 확인
        assertThat(tempUserRepository.findById(TEST_USERNAME)).isEmpty();
    }

    @Test
    @DisplayName("임시 유저가 없는 경우 회원가입 실패")
    void signUp_FailWhenTempUserNotFound() {
        // Given
        String nonExistentUsername = "KAKAO nonExistent";
        CreatorSignupRequest request = CreatorSignupRequest.builder()
                .nickname("creator")
                .email("creator@test.com")
                .build();

        // When & Then
        assertThatThrownBy(() -> userService.creatorSignUp(nonExistentUsername, request, response))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("userErrorCode", TEMP_USER_NOT_FOUND);
    }

    @Test
    @DisplayName("중복된 닉네임으로 회원가입 시도시 실패")
    void signUp_FailWhenNicknameDuplicated() {
        // Given
        String duplicateNickname = "duplicate";

        // 첫 번째 유저 생성
        CreatorSignupRequest firstRequest = CreatorSignupRequest.builder()
                .nickname(duplicateNickname)
                .email("first@test.com")
                .build();
        userService.creatorSignUp(TEST_USERNAME, firstRequest, response);

        // 두 번째 임시 유저 생성
        String secondUsername = "KAKAO testSocialId2";
        TempUser secondTempUser = TempUser.builder()
                .id(secondUsername)
                .socialId("testSocialId2")
                .socialProvider(SocialProvider.KAKAO)
                .profileImage("http://profile.image")
                .role(Role.ROLE_USER)
                .build();
        tempUserRepository.save(secondTempUser);

        // 두 번째 유저 생성 시도
        CreatorSignupRequest secondRequest = CreatorSignupRequest.builder()
                .nickname(duplicateNickname)
                .email("second@test.com")
                .build();

        // When & Then
        assertThatThrownBy(() -> userService.creatorSignUp(secondUsername, secondRequest, response))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("userErrorCode", DUPLICATED_NICKNAME);
    }
}