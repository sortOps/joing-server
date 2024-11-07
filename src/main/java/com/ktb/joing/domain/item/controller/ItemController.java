package com.ktb.joing.domain.item.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.item.dto.request.ItemCreateRequest;
import com.ktb.joing.domain.item.dto.response.ItemCreateResponse;
import com.ktb.joing.domain.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemCreateResponse> createItem(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody ItemCreateRequest request) {

        ItemCreateResponse response = itemService.createItem(request, customOAuth2User.getUsername());
        return ResponseEntity.ok(response);
    }
}
