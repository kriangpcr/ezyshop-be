package com.example.ezyshop.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductItemRequest {

    @NotBlank(message = "Product ID is required")
    private String product_id;

    @NotNull(message = "Data is required")
    @Size(min = 1, message = "Data must contain at least one item")
    private List<@NotBlank(message = "Data item must not be blank") String> data;
}
