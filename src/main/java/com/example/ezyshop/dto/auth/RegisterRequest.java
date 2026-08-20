package com.example.ezyshop.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 10, message = "Text must have at least 10 letters")
    private String username;

    @NotBlank
    @Size(min = 10, message = "Text must have at least 10 letters")
    private String password;
}
