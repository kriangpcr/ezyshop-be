package com.example.ezyshop.dto.product;

import com.example.ezyshop.enums.Category;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ProductResponse {

    private String id;

    private String title;

    private String description;

    private BigDecimal price;

    private Category category;

    private String imgUrl;

    private long sold;

    private long stock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
