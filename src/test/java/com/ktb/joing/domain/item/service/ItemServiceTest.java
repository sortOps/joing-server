package com.ktb.joing.domain.item.service;

import com.ktb.joing.domain.item.dto.request.EtcRequest;
import com.ktb.joing.domain.item.dto.request.ItemCreateRequest;
import com.ktb.joing.domain.item.dto.response.ItemCreateResponse;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.FavoriteCategory;
import com.ktb.joing.domain.user.entity.MediaType;
import com.ktb.joing.domain.user.entity.ProductManager;
import com.ktb.joing.domain.user.entity.Role;
import com.ktb.joing.domain.user.entity.SocialProvider;
import com.ktb.joing.domain.user.entity.User;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private ItemCreateRequest request;

    @BeforeEach
    void setUp() {

        ProductManager testUser = ProductManager.builder()
                .id(123L)
                .username("KAKAO testSocialId")
                .nickname("닉네임1")
                .email("email@naver.com")
                .profileImage("http://profileimage")
                .socialId("testSocialId")
                .socialProvider(SocialProvider.KAKAO)
                .role(Role.ROLE_USER)
                .build();

        // 선호 카테고리 생성 및 설정
        List<FavoriteCategory> favoriteCategories = Arrays.asList(
                FavoriteCategory.builder()
                        .user(testUser)
                        .category(Category.GAME)
                        .build(),
                FavoriteCategory.builder()
                        .user(testUser)
                        .category(Category.TECH)
                        .build(),
                FavoriteCategory.builder()
                        .user(testUser)
                        .category(Category.EDUCATION)
                        .build()
        );

        testUser.getFavoriteCategories().addAll(favoriteCategories);
        userRepository.save(testUser);
//        List<Category> categories = Arrays.asList(
//                Category.GAME,
//                Category.TECH,
//                Category.EDUCATION
//        );
//
//        // 테스트 사용자 생성 및 저장
//        ProductManager testUser = ProductManager.builder()
//                .id(123L)
//                .username("KAKAO testSocialId")
//                .nickname("닉네임1")
//                .email("email@naver.com")
//                .profileImage("http://profileimage")
//                .socialId("testSocialId")
//                .socialProvider(SocialProvider.KAKAO)
//                .role(Role.ROLE_USER)
//                .favoriteCategories(categories)
//                .build();

//        userRepository.save(testUser);

        // 테스트 요청 데이터 설정
        request = ItemCreateRequest.builder()
                .title("Test Title")
                .content("Test Content")
                .mediaType(MediaType.LONG_FORM)
                .category(Category.TECH)
                .etcs(Arrays.asList(
                        EtcRequest.builder()
                                .name("testName")
                                .value("testValue")
                                .build()
                ))
                .build();
    }

    @Nested
    @DisplayName("createItem 메소드는")
    class CreateItem {

        @Test
        @DisplayName("올바른 요청이 주어지면 아이템을 생성하고 응답을 반환한다")
        void createItem_WithValidRequest_ReturnsResponse() {
            // when
            ItemCreateResponse response = itemService.createItem(request, testUser.getUsername());

            // then
            assertThat(response).isNotNull();
            assertThat(response.getTitle()).isEqualTo(request.getTitle());
            assertThat(response.getContent()).isEqualTo(request.getContent());
            assertThat(response.getMediaType()).isEqualTo(request.getMediaType());
            assertThat(response.getCategory()).isEqualTo(request.getCategory());
            assertThat(response.getUserName()).isEqualTo(testUser.getUsername());
            assertThat(response.getScore()).isZero();

            // DB 저장 검증
            assertThat(itemRepository.findById(response.getId()))
                    .isPresent()
                    .get()
                    .satisfies(item -> {
                        assertThat(item.getTitle()).isEqualTo(request.getTitle());
                        assertThat(item.getContent()).isEqualTo(request.getContent());
                        assertThat(item.getUser().getUsername()).isEqualTo(testUser.getUsername());
                    });
        }

        @Test
        @DisplayName("존재하지 않는 사용자로 요청하면 예외가 발생한다")
        void createItem_WithNonExistentUser_ThrowsException() {
            // when & then
            assertThatThrownBy(() ->
                    itemService.createItem(request, "nonExistentUser"))
                    .isInstanceOf(UserException.class);

            // DB 저장 안됨 검증
            assertThat(itemRepository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("Etc 리스트가 없는 요청도 정상적으로 처리된다")
        void createItem_WithoutEtcs_CreatesItemSuccessfully() {
            // given
            ItemCreateRequest requestWithoutEtcs = ItemCreateRequest.builder()
                    .title("Test Title")
                    .content("Test Content")
                    .mediaType(MediaType.LONG_FORM)
                    .category(Category.TECH)
                    .build();

            // when
            ItemCreateResponse response = itemService.createItem(requestWithoutEtcs, testUser.getUsername());

            // then
            assertThat(response).isNotNull();
            assertThat(response.getEtcs()).isEmpty();

            // DB 저장 검증
            assertThat(itemRepository.findById(response.getId()))
                    .isPresent()
                    .get()
                    .satisfies(item -> {
                        assertThat(item.getEtcs()).isEmpty();
                    });
        }

        @Test
        @DisplayName("여러 개의 Etc를 포함한 요청이 정상적으로 처리된다")
        void createItem_WithMultipleEtcs_CreatesItemSuccessfully() {
            // given
            List<EtcRequest> multipleEtcs = Arrays.asList(
                    EtcRequest.builder().name("name1").value("value1").build(),
                    EtcRequest.builder().name("name2").value("value2").build(),
                    EtcRequest.builder().name("name3").value("value3").build()
            );

            ItemCreateRequest requestWithMultipleEtcs = ItemCreateRequest.builder()
                    .title("Test Title")
                    .content("Test Content")
                    .mediaType(MediaType.LONG_FORM)
                    .category(Category.TECH)
                    .etcs(multipleEtcs)
                    .build();

            // when
            ItemCreateResponse response = itemService.createItem(requestWithMultipleEtcs, testUser.getUsername());

            // then
            assertThat(response).isNotNull();
            assertThat(response.getEtcs()).hasSize(3);

            // DB 저장 검증
            assertThat(itemRepository.findById(response.getId()))
                    .isPresent()
                    .get()
                    .satisfies(item -> {
                        assertThat(item.getEtcs()).hasSize(3);
                        assertThat(item.getEtcs().get(0).getName()).isEqualTo("name1");
                        assertThat(item.getEtcs().get(0).getValue()).isEqualTo("value1");
                    });
        }
    }
}