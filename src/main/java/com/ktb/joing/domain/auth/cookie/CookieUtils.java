package com.ktb.joing.domain.auth.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CookieUtils {

    //특정 이름의 쿠키를 찾아 Optional<Cookie>로 반환하는 메서드
    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public Cookie createCookie(String name, String value) {

        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(24*60*60);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    // 특정 이름의 쿠키를 삭제하는 메서드
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String name) {
        Cookie[] cookies = request.getCookies();
        if (isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }


    // 쿠키 배열이 null이 아니고 길이가 0보다 큰지 확인하는 유틸리티 메서드
    private static boolean isNotEmpty(Cookie[] cookies) {
        return cookies != null && cookies.length > 0;
    }
}
