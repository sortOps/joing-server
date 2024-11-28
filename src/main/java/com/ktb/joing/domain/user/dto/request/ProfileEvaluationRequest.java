package com.ktb.joing.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileEvaluationRequest {
    private String channelId;

    @Builder
    public ProfileEvaluationRequest(String channelId) {
        this.channelId = channelId;
    }
}
