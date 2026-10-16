package com.example.ezyshop.dto.user;

import com.example.ezyshop.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private Role role;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
