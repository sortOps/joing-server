package com.ktb.joing.domain.item.dto.response;

import lombok.Getter;

@Getter
public class FeedbackView {
    private final String comment;

    public FeedbackView(String comment) {
        this.comment = comment;
    }
}
