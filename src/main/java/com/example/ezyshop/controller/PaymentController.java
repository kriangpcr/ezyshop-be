package com.example.ezyshop.controller;

import com.example.ezyshop.dto.payment.DepositRequest;
import com.example.ezyshop.dto.payment.PaymentResponse;
import com.example.ezyshop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/deposit")
    public ResponseEntity<PaymentResponse> deposit(@RequestBody DepositRequest request, Authentication authentication){
        return ResponseEntity.ok(paymentService.deposit(request,authentication));
    }
}
