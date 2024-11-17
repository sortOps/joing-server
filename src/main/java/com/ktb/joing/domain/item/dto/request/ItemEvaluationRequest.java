package com.ktb.joing.domain.item.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemEvaluationRequest {
    private String title;
    private String content;

    @JsonProperty("media_type")
    private String mediaType;

    @JsonProperty("proposal_score")
    private float proposalScore;

    @JsonProperty("additional_Features")
    private Map<String, String> additionalFeatures;

    @Builder
    public ItemEvaluationRequest(String title, String content, String mediaType,
                                 float proposalScore, Map<String, String> additionalFeatures) {
        this.title = title;
        this.content = content;
        this.mediaType = mediaType;
        this.proposalScore = proposalScore;
        this.additionalFeatures = additionalFeatures;
    }
}
