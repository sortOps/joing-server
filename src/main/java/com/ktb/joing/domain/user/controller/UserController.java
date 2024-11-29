package com.ktb.joing.domain.user.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.user.dto.request.CreatorSignupRequest;
import com.ktb.joing.domain.user.dto.request.ProductManagerSignupRequest;
import com.ktb.joing.domain.user.dto.request.UserUpdateRequest;
import com.ktb.joing.domain.user.dto.response.UserResponse;
import com.ktb.joing.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup/creator")
    public ResponseEntity<Void> creatorSignUp(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody @Valid CreatorSignupRequest creatorSignupRequest) {

        userService.creatorSignUp(customOAuth2User.getUsername(), creatorSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signup/productmanager")
    public ResponseEntity<Void> productManagerSignUp(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody @Valid ProductManagerSignupRequest productManagerSignupRequest){

        userService.productManagerSignUp(customOAuth2User.getUsername(), productManagerSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<UserResponse<?>> getUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        UserResponse<?> response = userService.getUser(customOAuth2User.getUsername());
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<UserResponse<?>> updateUser(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse<?> response = userService.updateUser(customOAuth2User.getUsername(), request);
        return ResponseEntity.ok(response);
    }

}
