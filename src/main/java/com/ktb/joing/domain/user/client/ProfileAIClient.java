package com.ktb.joing.domain.user.client;

import com.ktb.joing.common.util.webClient.ReactiveHttpService;
import com.ktb.joing.domain.user.dto.request.ProfileEvaluationRequest;
import com.ktb.joing.domain.user.dto.response.ProfileEvaluationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProfileAIClient {

    @Value("${ai.url}")
    private String aiUrl;

    private final ReactiveHttpService reactiveHttpService;


    public Mono<ProfileEvaluationResponse> profileEvaluation(ProfileEvaluationRequest request) {
        return reactiveHttpService.post(
                aiUrl + "/ai/evaluation/profile",
                request,
                ProfileEvaluationResponse.class
        ).doOnError(e -> log.error("프로필 평가 요청 실패: {}", e.getMessage()));
    }
}
