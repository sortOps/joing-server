package com.ktb.joing.domain.item.service;

import com.ktb.joing.domain.item.dto.response.SummaryResponse;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.entity.Summary;
import com.ktb.joing.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemSummaryService {
    private final ItemRepository itemRepository;

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
