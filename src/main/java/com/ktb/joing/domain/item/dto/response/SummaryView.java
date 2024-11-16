package com.ktb.joing.domain.item.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class SummaryView {
    private final String title;
    private final String content;
    private final List<String> keywords;

    public SummaryView(String title, String content, List<String> keywords) {
        this.title = title;
        this.content = content;
        this.keywords = keywords;
    }
}
