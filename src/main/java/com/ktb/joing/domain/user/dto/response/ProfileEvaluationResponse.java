package com.ktb.joing.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileEvaluationResponse {
    private boolean appropriate;
    private String reason;

    @Builder
    public ProfileEvaluationResponse(boolean appropriate, String reason) {
        this.appropriate = appropriate;
        this.reason = reason;
    }
}
