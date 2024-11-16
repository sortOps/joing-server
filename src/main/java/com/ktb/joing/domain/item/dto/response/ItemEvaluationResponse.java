package com.ktb.joing.domain.item.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemEvaluationResponse {
    @JsonProperty("evaluation_result")
    private int evaluationResult;
    private FeedbackResponse feedback;
    private SummaryResponse summary;
}