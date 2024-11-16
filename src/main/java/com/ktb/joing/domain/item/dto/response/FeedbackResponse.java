package com.ktb.joing.domain.item.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedbackResponse {
    @JsonProperty("feedback_type")
    private int feedbackType;

    @JsonProperty("current_score")
    private float currentScore;

    private String comment;
    private List<String> violations;

    public FeedbackView toView() {
        return new FeedbackView(this.comment);
    }
}
