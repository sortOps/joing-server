package com.ktb.joing.domain.user.exception;

import com.ktb.joing.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class UserException extends BusinessException {
    private final UserErrorCode userErrorCode;

    public UserException(UserErrorCode userErrorCode) {
        super(userErrorCode);
        this.userErrorCode = userErrorCode;
    }
}

