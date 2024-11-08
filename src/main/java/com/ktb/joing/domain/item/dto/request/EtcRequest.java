package com.ktb.joing.domain.item.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EtcRequest {
    private String name;
    private String value;

    @Builder
    public EtcRequest(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
