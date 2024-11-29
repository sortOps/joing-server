package com.ktb.joing.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponse<T> {
    private final UserType type;
    private final T data;

    public UserResponse(UserType type, T data) {
        this.type = type;
        this.data = data;
    }
}
