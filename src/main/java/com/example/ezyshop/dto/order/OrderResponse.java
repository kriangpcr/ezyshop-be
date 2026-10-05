package com.example.ezyshop.dto.order;

import com.example.ezyshop.enums.Category;
import com.example.ezyshop.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private String orderId;
    private String title;
    private Category category;
    private long quantity;
    private OrderStatus status;
    private List<OrderItemResponse> items;
}
