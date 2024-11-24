package com.ktb.joing.common.config;

import com.ktb.joing.domain.auth.handler.CustomSuccessHandler;
import com.ktb.joing.domain.auth.jwt.JwtFilter;
import com.ktb.joing.domain.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf 비횔성화
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 비활성화
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 비활성화
        http
                .httpBasic((auth) -> auth.disable());

        //oauth2 로그인 설정
        http
                .oauth2Login((oauth) -> oauth
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler));

        // JWT 필터 설정
        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // 경로별 인가 작업
        http.securityMatcher("/**") // 모든 요청에 대해
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/healthz", "/oauth2/**", "/login/**").permitAll()
                        .requestMatchers("/api/v1/users/signup/**").hasAuthority("TEMP_USER")
                        .anyRequest().authenticated()
                );

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
