package com.example.ezyshop.dto.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartRequest {

    private int quantity;

    private String product_id;

    private String cart_id;
}
