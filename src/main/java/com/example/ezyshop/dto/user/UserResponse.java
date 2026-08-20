package com.example.ezyshop.dto.user;

import com.example.ezyshop.dto.useraddress.UserAddressResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
