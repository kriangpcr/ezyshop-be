package com.example.ezyshop.controller;

import com.example.ezyshop.dto.user.UserResponse;
import com.example.ezyshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(Authentication authentication){
        UserResponse response =  userService.getUser(authentication);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/addbalance/{balance}")
    public ResponseEntity<Void> addBalance(@PathVariable BigDecimal balance, Authentication authentication){
        userService.addBalance(balance,authentication);
        return ResponseEntity.noContent().build();
    }
}
