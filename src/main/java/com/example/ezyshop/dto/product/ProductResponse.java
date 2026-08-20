package com.example.ezyshop.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {

    private String id;

    private String title;

    private String description;

    private BigDecimal price;

    private String category;

    private String productImg;

    private String imgUrl;

    private int stock;

    private int sold;

    private String user_id;

    private String sellerName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
