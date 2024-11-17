package com.ktb.joing.common.util.webClient;

import com.ktb.joing.common.exception.AiErrorCode;
import com.ktb.joing.common.exception.AiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveHttpService {
    private final WebClient webClient;

     public <T, R> Mono<R> post(String url, T body, Class<R> responseType) {
        return webClient.post()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    throw new AiException(AiErrorCode.AI_BAD_GATEWAY);
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    throw new AiException(AiErrorCode.AI_SEVER_ERROR);
                })
                .bodyToMono(responseType);
    }

}
