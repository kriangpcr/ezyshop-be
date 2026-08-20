package com.example.ezyshop.dto.order;

import com.example.ezyshop.dto.product.ProductResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemResponse {
    private String id;
    private String productName;
    private String productImageUrl;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
