package com.ktb.joing.domain.item.service;

import com.ktb.joing.common.util.webClient.ReactiveHttpService;
import com.ktb.joing.domain.item.dto.request.ItemEvaluationRequest;
import com.ktb.joing.domain.item.dto.response.EvaluationResponse;
import com.ktb.joing.domain.item.dto.response.ItemEvaluationResponse;
import com.ktb.joing.domain.item.dto.response.ResponseType;
import com.ktb.joing.domain.item.entity.Etc;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.exception.ItemErrorCode;
import com.ktb.joing.domain.item.exception.ItemException;
import com.ktb.joing.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemEvaluationService {

    @Value("${ai.url}")
    private String aiUrl;

    private final ItemRepository itemRepository;
    private final ReactiveHttpService reactiveHttpService;
    private final ItemSummaryService itemSummaryService;

    public Mono<EvaluationResponse<?>> requestEvaluation(Long itemId, String username) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.getProductManager().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }

        ItemEvaluationRequest request = createEvaluationRequest(item);

        return reactiveHttpService.post(
                        aiUrl + "/ai/evaluation/proposal",
                        request,
                        ItemEvaluationResponse.class
                )
                .doOnSuccess(response -> {
                    if (response.getEvaluationResult() == 1) {
                        itemSummaryService.updateItemSummary(item, response.getSummary());
                    }
                })
                .<EvaluationResponse<?>>map(this::convertToEvaluationResponse)
                .doOnError(e -> log.error("AI 평가 요청 실패: {}", e.getMessage()))
                .onErrorMap(e -> new ItemException(ItemErrorCode.AI_EVALUATION_FAILED));
    }

    private EvaluationResponse<?> convertToEvaluationResponse(ItemEvaluationResponse response) {
        if (response.getEvaluationResult() == 0) {
            return new EvaluationResponse<>(ResponseType.FEEDBACK,
                    response.getFeedback().toView());
        } else {
            return new EvaluationResponse<>(ResponseType.SUMMARY,
                    response.getSummary().toView());
        }
    }

    private ItemEvaluationRequest createEvaluationRequest(Item item) {
        return ItemEvaluationRequest.builder()
                .title(item.getTitle())
                .content(item.getContent())
                .mediaType(item.getMediaType().toString().toLowerCase())
                .proposalScore(item.getScore())
                .additionalFeatures(item.getEtcs().stream()
                        .collect(Collectors.toMap(
                                Etc::getName,
                                Etc::getValue
                        )))
                .build();
    }
}
