package com.example.ezyshop.controller;

import com.example.ezyshop.dto.auth.RegisterRequest;
import com.example.ezyshop.dto.cart.CartRequest;
import com.example.ezyshop.dto.cart.CartResponse;
import com.example.ezyshop.service.CartServcie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartServcie cartServcie;

    @PostMapping("/add")
    public ResponseEntity<Void> addCart(@Valid @RequestBody CartRequest request,Authentication authentication){
        cartServcie.addCart(request,authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get")
    public ResponseEntity<CartResponse> getCart(Authentication authentication){
        cartServcie.getCart(authentication);
        return ResponseEntity.ok(cartServcie.getCart(authentication));
    }

    @PutMapping("/minus/{id}")
    public ResponseEntity<Void> minusCart(@PathVariable String id,Authentication authentication){
        cartServcie.minusCart(id,authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/plus/{id}")
    public ResponseEntity<Void> plusCart(@PathVariable String id,Authentication authentication){
        cartServcie.plusCart(id,authentication);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id,Authentication authentication){
        cartServcie.deleteCart(id,authentication);
        return ResponseEntity.noContent().build();
    }
}
