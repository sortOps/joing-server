package com.ktb.joing.domain.item.client;

import com.ktb.joing.common.util.webClient.ReactiveHttpService;
import com.ktb.joing.domain.item.dto.request.ItemEvaluationRequest;
import com.ktb.joing.domain.item.dto.response.ItemEvaluationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemAIClient {

    @Value("${ai.url}")
    private String aiUrl;

    private final ReactiveHttpService reactiveHttpService;

    public Mono<ItemEvaluationResponse> requestEvaluation(ItemEvaluationRequest request) {
        return reactiveHttpService.post(
                aiUrl + "/ai/evaluation/proposal",
                request,
                ItemEvaluationResponse.class
        ).doOnError(e -> log.error("기획안 평가 요청 실패: {}", e.getMessage()));
    }

    public Mono<ItemEvaluationResponse> regenerateSummary(ItemEvaluationRequest request) {
        return reactiveHttpService.post(
                aiUrl + "/ai/generation/summary",
                request,
                ItemEvaluationResponse.class
        ).doOnError(e -> log.error("기획안 요약 생성 실패: {}", e.getMessage()));
    }
}
