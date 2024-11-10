package com.ktb.joing.domain.item.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.item.dto.request.ItemCreateRequest;
import com.ktb.joing.domain.item.dto.request.ItemUpdateRequest;
import com.ktb.joing.domain.item.dto.response.ItemResponse;
import com.ktb.joing.domain.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<ItemResponse> createItem(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody ItemCreateRequest request) {

        ItemResponse response = itemService.createItem(request, customOAuth2User.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long itemId) {
        ItemResponse response = itemService.getItem(itemId);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody ItemUpdateRequest request) {

        ItemResponse response = itemService.updateItem(itemId, request, customOAuth2User.getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        itemService.deleteItem(itemId, customOAuth2User.getUsername());
        return ResponseEntity.noContent().build();
    }
}
