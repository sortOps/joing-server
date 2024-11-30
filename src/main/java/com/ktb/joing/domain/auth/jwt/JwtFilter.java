package com.ktb.joing.domain.auth.jwt;

import com.ktb.joing.domain.auth.cookie.CookieUtils;
import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.auth.dto.UserDto;
import com.ktb.joing.domain.auth.exception.AuthErrorCode;
import com.ktb.joing.domain.auth.exception.AuthException;
import com.ktb.joing.domain.user.entity.Role;
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
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CookieUtils cookieUtils;
    private final TokenService tokenService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher(); // 추가

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 인증이 필요없는 경로들 정의
        String[] permitUrls = {
                "/",
                "/healthz",
                "/oauth2/**",
                "/login/**",
                "/favicon.ico"
        };

        boolean result = Arrays.stream(permitUrls)
                .anyMatch(permitUrl -> pathMatcher.match(permitUrl, path));

        log.info("JwtFilter shouldNotFilter({}) : {}", path, result);
        return result;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Request URI: {}", request.getRequestURI()); //첫 진입점에서 요청 정보 확인
        // jwt 기간 만료시, 무한 재로그인 방지 로직
        if (isOAuth2RelatedPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader("access");
        Optional<Cookie> refreshTokenCookie = cookieUtils.getCookie(request, "refresh");
        String refreshToken = refreshTokenCookie.map(Cookie::getValue).orElse(null);
        log.info("Access token: {}", accessToken);
        log.info("Refresh token: {}", refreshToken);

        // 1. 토큰이 모두 없는 경우
        if (accessToken == null && refreshToken == null) {
            log.info("No tokens present");
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Access 토큰 카테고리 검증
        if (accessToken != null && !jwtUtil.getCategoryFromToken(accessToken).equals("access")) {
            log.error("Invalid access token category");
            throw new AuthException(AuthErrorCode.INVALID_JWT);
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

    }

    //jwt 기간 만료시, 무한 재로그인 방지 로직
    private boolean isOAuth2RelatedPath(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return requestUri.matches("^/login(?:/.*)?$") ||
                requestUri.matches("^/oauth2(?:/.*)?$");
    }


    private void setAuthentication(String username, String role) {
        CustomOAuth2User userDetails = createUserDetails(username, role);
        Authentication authentication = getAuthentication(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //스프링 시큐리티 인증 토큰 생성
    private Authentication getAuthentication(CustomOAuth2User userDetails){
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    // UserDetails 객체 생성
    private CustomOAuth2User createUserDetails(String username, String role) {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setRole(Role.valueOf(role));
        return new CustomOAuth2User(userDto);
    }

}
