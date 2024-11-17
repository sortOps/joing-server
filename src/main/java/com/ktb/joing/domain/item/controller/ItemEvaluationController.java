package com.ktb.joing.domain.item.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.item.dto.response.EvaluationResponse;
import com.ktb.joing.domain.item.service.ItemEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemEvaluationController {
    private final ItemEvaluationService itemEvaluationService;

    @GetMapping("/{itemId}/evaluation")
    public Mono<ResponseEntity<EvaluationResponse<?>>> requestEvaluation(
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        return itemEvaluationService.requestEvaluation(itemId, customOAuth2User.getUsername())
                .map(ResponseEntity::ok);
    }
}