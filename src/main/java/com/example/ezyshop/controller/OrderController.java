package com.example.ezyshop.controller;

import com.example.ezyshop.dto.order.CreateOrderRequest;
import com.example.ezyshop.dto.order.OrderItemResponse;
import com.example.ezyshop.dto.user.UserResponse;
import com.example.ezyshop.entity.Order;
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

    @PostMapping(path = "/create")
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest request, Authentication authentication){
        orderService.createOrder(request,authentication);
        return ResponseEntity.ok("Success");
    }

    @GetMapping(path = "/get")
    public ResponseEntity<List<OrderItemResponse>> getOrders(Authentication authentication){
        return ResponseEntity.ok(orderService.getOrders(authentication));
    }

}
