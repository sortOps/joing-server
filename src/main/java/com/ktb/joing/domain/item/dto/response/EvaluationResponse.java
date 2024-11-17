package com.ktb.joing.domain.item.dto.response;

import lombok.Getter;

@Getter
public class EvaluationResponse<T> {
    private final ResponseType type;
    private final T data;

    public EvaluationResponse(ResponseType type, T data) {
        this.type = type;
        this.data = data;
    }
}
