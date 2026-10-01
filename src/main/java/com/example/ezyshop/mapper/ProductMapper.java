package com.example.ezyshop.mapper;

import com.example.ezyshop.dto.product.ProductRequest;
import com.example.ezyshop.dto.product.ProductResponse;
import com.example.ezyshop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(Product product);

    Product toEntity(ProductRequest request);
}
