package com.ktb.joing.domain.item.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummaryResponse {
    private String title;
    private String content;
    @JsonProperty("keyword")
    private List<String> keywords;

    public SummaryView toView() {
        return new SummaryView(this.title, this.content, this.keywords);
    }
}
