package com.ktb.joing.domain.auth.exception;

import com.ktb.joing.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class InvalidJwtException extends BusinessException {
    private final AuthErrorCode authErrorCode;

    public InvalidJwtException(AuthErrorCode authErrorCode){
        super(authErrorCode);
        this.authErrorCode = authErrorCode;
    }
}
