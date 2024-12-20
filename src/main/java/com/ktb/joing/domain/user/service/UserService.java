package com.ktb.joing.domain.user.service;

import com.ktb.joing.domain.auth.entity.TempUser;
import com.ktb.joing.domain.auth.repository.TempUserRepository;
import com.ktb.joing.domain.user.dto.request.CreatorSignupRequest;
import com.ktb.joing.domain.user.dto.request.ProductManagerSignupRequest;
import com.ktb.joing.domain.user.entity.Creator;
import com.ktb.joing.domain.user.entity.FavoriteCategory;
import com.ktb.joing.domain.user.entity.ProductManager;
import com.ktb.joing.domain.user.entity.Role;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final TempUserRepository tempUserRepository;

    public void creatorSignUp(String username, CreatorSignupRequest request) {
        TempUser tempUser = tempUserRepository.findById(username)
                .orElseThrow(() -> new UserException(UserErrorCode.TEMP_USER_NOT_FOUND));

        validateDuplicateNickname(request.getNickname());

        Creator creator = Creator.builder()
                .username(tempUser.getId())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .profileImage(tempUser.getProfileImage())
                .profileSetup(true)
                .role(Role.ROLE_USER)
                .socialId(tempUser.getSocialId())
                .socialProvider(tempUser.getSocialProvider())
                .channelUrl(request.getChannelUrl())
                .mediaType(request.getMediaType())
                .category(request.getCategory())
                .build();

        userRepository.save(creator);

        tempUserRepository.deleteById(username);
    }

    public void productManagerSignUp(String username, ProductManagerSignupRequest request) {
        TempUser tempUser = tempUserRepository.findById(username)
                .orElseThrow(() -> new UserException(UserErrorCode.TEMP_USER_NOT_FOUND));

        validateDuplicateNickname(request.getNickname());

        ProductManager user = ProductManager.builder()
                .username(tempUser.getId())
                .profileImage(tempUser.getProfileImage())
                .socialId(tempUser.getSocialId())
                .socialProvider(tempUser.getSocialProvider())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .profileSetup(true)
                .role(Role.ROLE_USER)
                .build();

        request.getFavoriteCategories().forEach(category -> {
            FavoriteCategory favoriteCategory = FavoriteCategory.builder()
                    .category(category)
                    .build();
            user.addFavoriteCategory(favoriteCategory);
        });

        userRepository.save(user);

        tempUserRepository.deleteById(username);
    }

    // 닉네임 중복 확인
    private void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(UserErrorCode.DUPLICATED_NICKNAME);
        }
    }

}
