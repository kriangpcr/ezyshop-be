package com.example.ezyshop.controller;

import com.example.ezyshop.dto.auth.LoginRequest;
import com.example.ezyshop.dto.auth.LoginResponse;
import com.example.ezyshop.dto.auth.RegisterRequest;
import com.example.ezyshop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResponse response = new LoginResponse();
        response.setToken(authService.login(request));
        return ResponseEntity.ok(response);
    }


}
