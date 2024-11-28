package com.ktb.joing.domain.user.exception;

import com.ktb.joing.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    // User Error
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다"),
    TEMP_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "임시 저장된 사용자 정보가 없습니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다"),
    PROFILE_EVALUATION_FAILED(HttpStatus.BAD_GATEWAY, "AI 프로필 평가 요청에 실패했습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
