package com.example.ezyshop.dto.order;

import com.example.ezyshop.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {

    private String id;

    private List<OrderItemResponse> items;

    private BigDecimal totalPrice;

    private OrderStatus status;

}
