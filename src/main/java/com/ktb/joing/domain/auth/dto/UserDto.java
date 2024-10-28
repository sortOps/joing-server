package com.ktb.joing.domain.auth.dto;

import com.ktb.joing.domain.user.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String name;
    private Role role;
}
