package com.ktb.joing.domain.item.exception;

import com.ktb.joing.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemErrorCode implements ErrorCode {
    // Item Error
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다"),
    INVALID_USER_TYPE(HttpStatus.BAD_REQUEST, "상품 기획자만 아이템을 생성할 수 있습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
