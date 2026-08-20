package com.example.ezyshop.dto.cart;

import com.example.ezyshop.dto.product.ProductResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemResponse {

    private String id;

    private int quantity;

    private ProductResponse product;
}
