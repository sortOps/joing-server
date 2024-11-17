package com.ktb.joing.common.exception;

import lombok.Getter;

@Getter
public class AiException extends BusinessException {
    private final AiErrorCode aiErrorCode;

    public AiException(AiErrorCode aiErrorCode) {
        super(aiErrorCode);
        this.aiErrorCode = aiErrorCode;
    }
}
