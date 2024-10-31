package com.ktb.joing.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final String SECRET_KEY;
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 6; //6시간
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14; //2주

    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰에 대한 검증 메소드
    public Boolean isTokenValid(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT token 입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public String getUsernameFromToken(String token) {

        return getClaimsFromToken(token).get("username", String.class);
    }

    public String getRoleFromToken(String token) {

        return getClaimsFromToken(token).get("role", String.class);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public String getCategoryFromToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String createAccessToken(String category, String username, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(getSigningKey()).compact();
    }

    public String createRefreshToken(String category, String username, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);
        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(getSigningKey()).compact();
    }
}
