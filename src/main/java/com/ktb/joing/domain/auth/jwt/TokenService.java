package com.ktb.joing.domain.auth.jwt;

import com.ktb.joing.domain.auth.exception.AuthErrorCode;
import com.ktb.joing.domain.auth.exception.AuthException;
import com.ktb.joing.domain.auth.redis.TempUser;
import com.ktb.joing.domain.auth.redis.TempUserRepository;
import com.ktb.joing.domain.user.entity.User;
import com.ktb.joing.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TempUserRepository tempUserRepository;
    private final JwtUtil jwtUtil;

    // 리프레시 토큰을 이용해 새로운 액세스 토큰을 발급하고 응답 헤더에 설정
    public String reissueAccessToken(HttpServletResponse response, String refreshToken) {
        String username = getUsernameFromRefreshToken(refreshToken);
        String renewAccessToken = null;

        // 임시 회원인지 확인하고 해당하는 토큰 발급
        if (tempUserRepository.findById(username).isPresent()) {
            renewAccessToken = createTempAccessToken(refreshToken);
        } else {
            renewAccessToken = createAccessToken(refreshToken);
        }

        response.setHeader("access", renewAccessToken);
        return renewAccessToken;
    }

    // 리프레시 토큰으로부터 새로운 액세스 토큰 생성
    public String createAccessToken(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_JWT));

        User user = userRepository.findByUsername(findRefreshToken.getUsername())
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));


        return jwtUtil.createAccessToken(
                "access",
                user.getUsername(),
                user.getRole().name()
        );
    }

    // 임시 회원용 액세스 토큰 생성
    private String createTempAccessToken(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_JWT));

        TempUser tempUser =  tempUserRepository.findById(findRefreshToken.getUsername())
                .orElseThrow(() -> new AuthException(AuthErrorCode.TEMP_USER_NOT_FOUND));

        return jwtUtil.createTempAccessToken(
                "access",
                tempUser.getId(),
                tempUser.getRole().name()
        );
    }

    // 리프레시 토큰에서 username 추출
    private String getUsernameFromRefreshToken(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_JWT));
        return findRefreshToken.getUsername();
    }

    // 리프레시 토큰 저장
    public void saveRefreshToken(String username, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(username, refreshToken));
    }

    //리프레시 토큰 삭제
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }


}