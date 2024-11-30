package com.ktb.joing.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileEvaluationRequest {
    private String channelId;

    public ProfileEvaluationRequest(String channelId) {
        this.channelId = channelId;
    }
}
