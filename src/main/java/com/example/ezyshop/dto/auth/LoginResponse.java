package com.example.ezyshop.dto.auth;

import com.example.ezyshop.dto.user.UserResponse;
import com.example.ezyshop.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private UserResponse user;
    private String accessToken;
    private String refreshToken;
}
