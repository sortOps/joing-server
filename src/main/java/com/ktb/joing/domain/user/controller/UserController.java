package com.ktb.joing.domain.user.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.user.dto.request.CreatorSignupRequest;
import com.ktb.joing.domain.user.dto.request.ProductManagerSignupRequest;
import com.ktb.joing.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestBody @Valid CreatorSignupRequest creatorSignupRequest, HttpServletResponse response) {

        userService.creatorSignUp(customOAuth2User.getUsername(), creatorSignupRequest, response);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signup/productmanager")
    public ResponseEntity<Void> productManagerSignUp(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody @Valid ProductManagerSignupRequest productManagerSignupRequest, HttpServletResponse response){

        userService.productManagerSignUp(customOAuth2User.getUsername(), productManagerSignupRequest, response);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}