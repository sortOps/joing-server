package com.ktb.joing.domain.item.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EtcRequest {

    @Size(max = 50)
    private String name;
    @Size(max = 200)
    private String value;

    @Builder
    public EtcRequest(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
