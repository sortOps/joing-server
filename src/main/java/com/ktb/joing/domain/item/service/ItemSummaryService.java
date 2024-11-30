package com.ktb.joing.domain.item.service;

import com.ktb.joing.domain.item.client.ItemAIClient;
import com.ktb.joing.domain.item.dto.request.ItemEvaluationRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemSummaryService {

    private final ItemRepository itemRepository;
    private final ItemAIClient itemAIClient;

    public Mono<SummaryView> regenerateSummary(Long itemId, String username) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.getProductManager().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }

        ItemEvaluationRequest request = createSummaryRequest(item);

        return itemAIClient.regenerateSummary(request)
                .map(response -> {
                    updateItemSummary(item, response.getSummary());
                    return response.getSummary().toView();
                })
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
