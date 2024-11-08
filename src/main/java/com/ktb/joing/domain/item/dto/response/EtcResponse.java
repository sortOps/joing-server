package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Etc;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EtcResponse {
    private String name;
    private String value;

    @Builder
    public EtcResponse(Etc etc) {
        this.name = etc.getName();
        this.value = etc.getValue();
    }
}
