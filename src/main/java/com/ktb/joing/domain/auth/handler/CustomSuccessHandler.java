package com.ktb.joing.domain.auth.handler;

import com.ktb.joing.domain.auth.cookie.CookieUtils;
import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.auth.jwt.JwtUtil;

import com.ktb.joing.domain.auth.jwt.TokenService;
import com.ktb.joing.domain.auth.repository.TempUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${app.frontend.url}")
    private String frontUrl;

    private final JwtUtil jwtUtil;
    private final CookieUtils cookieUtils;
    private final TokenService tokenService;
    private final TempUserRepository tempUserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        String role = extractRole(authentication);

        String targetUrl = determineTargetUrl(username, role, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 인증 객체에서 역할 정보를 추출
    private String extractRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        return auth.getAuthority();
    }

    // 사용자 상태에 따라 적절한 리다이렉트 URL 결정
    private String determineTargetUrl(String username, String role, HttpServletResponse response) {
        if (tempUserRepository.findById(username).isPresent()) {
            return createSignupRedirectUrl(username, role, response);
        }
        return createMainRedirectUrl(username, role, response);
    }

    // 회원가입 페이지로의 리다이렉트 URL 생성
    private String createSignupRedirectUrl(String username, String role, HttpServletResponse response) {
        String access = jwtUtil.createTempAccessToken("access", username, role);
        String refresh = jwtUtil.createTempRefreshToken("refresh", username, role);
        tokenService.saveRefreshToken(username, refresh);
        response.addCookie(cookieUtils.createCookie("refresh", refresh));
        log.info("access token: {}", access);
        log.info("refresh token: {}", refresh);

        return UriComponentsBuilder.fromUriString(frontUrl + "/signup")
                .queryParam("token", access)
                .build()
                .toUriString();
    }

    // 메인 페이지로의 리다이렉트 URL 생성
    private String createMainRedirectUrl(String username, String role, HttpServletResponse response) {
        String access = jwtUtil.createAccessToken("access", username, role);
        String refresh = jwtUtil.createRefreshToken("refresh", username, role);
        tokenService.saveRefreshToken(username, refresh);
        response.addCookie(cookieUtils.createCookie("refresh", refresh));
        log.info("access token: {}", access);
        log.info("refresh token: {}", refresh);

        return UriComponentsBuilder.fromUriString(frontUrl)
                .queryParam("token", access)
                .build()
                .toUriString();
    }

}
