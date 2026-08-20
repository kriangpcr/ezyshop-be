package com.example.ezyshop.mapper;

import com.example.ezyshop.dto.cart.CartResponse;
import com.example.ezyshop.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toResponse(Cart cart);
}
