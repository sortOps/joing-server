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
    INVALID_USER_TYPE(HttpStatus.BAD_REQUEST, "상품 기획자만 아이템을 생성할 수 있습니다"),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "기획안을 찾을 수 없습니다."),
    ITEM_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "해당 기획안에 대한 수정 권한이 없습니다."),
    AI_EVALUATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 평가 요청에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
