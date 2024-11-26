package com.ktb.joing.domain.auth.jwt;

import com.ktb.joing.domain.auth.cookie.CookieUtils;
import com.ktb.joing.domain.auth.exception.AuthErrorCode;
import com.ktb.joing.domain.auth.exception.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {

    private static final String LOGOUT_PATH = "/api/v1/logout";

    private final JwtUtil jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtils cookieUtils;

    // 로그아웃 요청을 처리하는 필터 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!isLogoutRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String refreshToken = cookieUtils.getCookie(request, "refresh")
                    .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_JWT))
                    .getValue();

            validateRefreshToken(refreshToken);
            processLogout(refreshToken, request, response);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (AuthException e) {
            log.error("Logout failed: {}", e.getMessage());
            response.setStatus(e.getAuthErrorCode().getHttpStatus().value());
        }
    }

    // 현재 요청이 로그아웃 요청인지 확인
    private boolean isLogoutRequest(HttpServletRequest request) {
        return request.getRequestURI().equals(LOGOUT_PATH)
                && request.getMethod().equals(HttpMethod.POST.name());
    }

    // Refresh 토큰의 유효성을 검증
    private void validateRefreshToken(String refreshToken) {
        if (!jwtUtils.isTokenValid(refreshToken)) {
            throw new AuthException(AuthErrorCode.INVALID_JWT);
        }

        if (!refreshTokenRepository.existsById(refreshToken)) {
            throw new AuthException(AuthErrorCode.TOKEN_NOT_FOUND);
        }
    }

    // 실제 로그아웃 처리를 수행 (Redis에서 refresh 토큰을 삭제하고 쿠키를 제거)
    private void processLogout(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        refreshTokenRepository.deleteById(refreshToken);
        cookieUtils.deleteCookie(request, response, "refresh");
    }
}
