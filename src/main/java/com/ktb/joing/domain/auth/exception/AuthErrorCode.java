package com.ktb.joing.domain.auth.exception;

import com.ktb.joing.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor

public enum AuthErrorCode implements ErrorCode {
    // Auth Error
    OAUTH2_LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "로그인 실패"),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    TEMP_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "임시 사용자 정보를 찾을 수 없습니다"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다"),
    AUTHENTICATION_ENTRY_POINT(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
    REDIRECT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다");

    private final HttpStatus httpStatus;
    private final String message;
}
