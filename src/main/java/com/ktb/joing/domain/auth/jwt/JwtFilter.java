package com.ktb.joing.domain.auth.jwt;

import com.ktb.joing.domain.auth.cookie.CookieUtils;
import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.auth.exception.AuthErrorCode;
import com.ktb.joing.domain.auth.exception.InvalidJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CookieUtils cookieUtils;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // jwt 기간 만료시, 무한 재로그인 방지 로직
        if (isOAuth2RelatedPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader("access");
        Optional<Cookie> refreshTokenCookie = cookieUtils.getCookie(request, "refresh");
        String refreshToken = refreshTokenCookie.map(Cookie::getValue).orElse(null);

        try {
            // 1. 토큰이 모두 없는 경우
            if (accessToken == null && refreshToken == null) {
                log.info("No tokens present");
                filterChain.doFilter(request, response);
                return;
            }

            // 2. Access 토큰 카테고리 검증
            if (accessToken != null && !jwtUtil.getCategoryFromToken(accessToken).equals("access")) {
                log.error("Invalid access token category");
                throw new InvalidJwtException(AuthErrorCode.INVALID_JWT);
            }

            // 3. Access 토큰이 유효한 경우
            if (accessToken != null && jwtUtil.isTokenValid(accessToken)) {
                log.info("Valid access token");
                setAuthentication(
                        jwtUtil.getUsernameFromToken(accessToken),
                        jwtUtil.getRoleFromToken(accessToken)
                );
                filterChain.doFilter(request, response);
                return;
            }

            // 4. Access 토큰이 만료되고 Refresh 토큰이 유효한 경우
            if (refreshToken != null && jwtUtil.isTokenValid(refreshToken)) {
                log.info("Access token expired, but refresh token valid");
                String newAccessToken = tokenService.reissueAccessToken(response, refreshToken);
                setAuthentication(
                        jwtUtil.getUsernameFromToken(newAccessToken),
                        jwtUtil.getRoleFromToken(newAccessToken)
                );
                filterChain.doFilter(request, response);
                return;
            }

            // 5. Refresh 토큰도 만료된 경우
            if (refreshToken != null) {
                log.info("Both tokens expired");
                cookieUtils.deleteCookie(request, response, "refresh");
                tokenService.deleteRefreshToken(refreshToken);
            }

            // 6. 그 외의 경우 (유효하지 않은 토큰)
            log.info("Invalid tokens");
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT Filter Error", e);
            cookieUtils.deleteCookie(request, response, "refresh");
            throw e;
        }
    }

    //jwt 기간 만료시, 무한 재로그인 방지 로직
    private boolean isOAuth2RelatedPath(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return requestUri.matches("^\\/login(?:\\/.*)?$") ||
                requestUri.matches("^\\/oauth2(?:\\/.*)?$");
    }

    private void setAuthentication(String username, String role) {
        CustomOAuth2User customOAuth2User = getUserDetails(username, role);
        Authentication authentication = getAuthentication(customOAuth2User);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //스프링 시큐리티 인증 토큰 생성
    private Authentication getAuthentication(CustomOAuth2User userDetails){
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // UserDetails 객체 생성
    private CustomOAuth2User getUserDetails(String username, String role){
        return (CustomOAuth2User) User.builder()
                .username(username)
                .password("")
                .authorities(role)
                .build();
    }
}
