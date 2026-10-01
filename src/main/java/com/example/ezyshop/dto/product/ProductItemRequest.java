package com.example.ezyshop.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductItemRequest {
    private String product_id;
    private List<String> data;
}
