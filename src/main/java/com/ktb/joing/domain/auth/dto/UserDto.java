package com.ktb.joing.domain.auth.dto;

import com.ktb.joing.domain.user.entity.Role;
import com.ktb.joing.domain.user.entity.SocialProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String name;
    private SocialProvider provider;
    private String providerId;
    private String profileImage;
    private Role role;
}
