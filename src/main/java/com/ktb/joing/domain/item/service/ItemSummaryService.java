package com.ktb.joing.domain.item.service;

import com.ktb.joing.common.util.webClient.ReactiveHttpService;
import com.ktb.joing.domain.item.dto.request.ItemEvaluationRequest;
import com.ktb.joing.domain.item.dto.response.ItemEvaluationResponse;
import com.ktb.joing.domain.item.dto.response.SummaryResponse;
import com.ktb.joing.domain.item.dto.response.SummaryView;
import com.ktb.joing.domain.item.entity.Etc;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.entity.Summary;
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
public class ItemSummaryService {

    @Value("${ai.url}")
    private String aiUrl;

    private final ItemRepository itemRepository;
    private final ReactiveHttpService reactiveHttpService;

    public Mono<SummaryView> regenerateSummary(Long itemId, String username) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.getUser().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }

        ItemEvaluationRequest request = createSummaryRequest(item);

        return reactiveHttpService.post(
                        aiUrl + "/ai/generation/summary",
                        request,
                        ItemEvaluationResponse.class
                )
                .map(response -> {
                    updateItemSummary(item, response.getSummary());
                    return response.getSummary().toView();
                })
                .doOnError(e -> log.error("Summary 재생성 요청 실패: {}", e.getMessage()))
                .onErrorMap(e -> new ItemException(ItemErrorCode.AI_EVALUATION_FAILED));
    }

    private ItemEvaluationRequest createSummaryRequest(Item item) {
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


    public void updateItemSummary(Item item, SummaryResponse summaryData) {
        Summary summary = Summary.builder()
                .title(summaryData.getTitle())
                .content(summaryData.getContent())
                .keyword(String.join(",", summaryData.getKeywords()))
                .build();
        item.setSummary(summary);
        itemRepository.save(item);
    }
}
