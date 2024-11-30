package com.ktb.joing.common.config;

import com.ktb.joing.domain.auth.handler.CustomSuccessHandler;
import com.ktb.joing.domain.auth.jwt.CustomLogoutFilter;
import com.ktb.joing.domain.auth.jwt.JwtFilter;
import com.ktb.joing.domain.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtFilter jwtFilter;
    private final CustomLogoutFilter customLogoutFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // cors 설정
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

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
        http
                .addFilterBefore(customLogoutFilter, LogoutFilter.class);

        // 경로별 인가 작업
        http.securityMatcher("/**") // 모든 요청에 대해
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/healthz", "/oauth2/**", "/login/**").permitAll()
                        .requestMatchers("/signup/**", "/api/v1/users/signup/**").hasRole("TEMP_USER")
                        .anyRequest().authenticated()
                );

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(frontendUrl));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
