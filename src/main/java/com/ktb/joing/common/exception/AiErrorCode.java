package com.ktb.joing.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AiErrorCode implements ErrorCode {
    //AI
    AI_BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "AI 서버와의 통신에 실패하였습니다."),
    AI_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI 서버 내부에서 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
