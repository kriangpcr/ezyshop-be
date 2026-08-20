package com.example.ezyshop.controller;

import com.example.ezyshop.dto.order.OrderRequest;
import com.example.ezyshop.dto.order.OrderResponse;
import com.example.ezyshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/get")
    public ResponseEntity<List<OrderResponse>> getOrder(Authentication authentication){
        return ResponseEntity.ok(orderService.getOrders(authentication));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequest request, Authentication authentication){
        orderService.createOrder(request,authentication);
        return ResponseEntity.noContent().build();
    }
}
