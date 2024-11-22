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
        //유저 정보
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 임시 데이터가 있다면 신규 회원가입 진행
        if (tempUserRepository.findById(username).isPresent()) {
            response.sendRedirect(frontUrl + "/signup");
            return;
        }

        // 정상 회원용 토큰 생성
        String access = jwtUtil.createAccessToken("access", username, role);
        String refresh = jwtUtil.createRefreshToken("refresh", username, role);

        // Refresh 토큰 저장
        tokenService.saveRefreshToken(username, refresh);

        // 응답 설정
        response.setHeader("access", access);
        response.addCookie(cookieUtils.createCookie("refresh", refresh));

        response.sendRedirect(frontUrl);
    }

}
