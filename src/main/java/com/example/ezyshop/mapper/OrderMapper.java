package com.example.ezyshop.mapper;

import com.example.ezyshop.dto.order.OrderItemResponse;
import com.example.ezyshop.dto.order.OrderResponse;
import com.example.ezyshop.entity.Order;
import com.example.ezyshop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);

    @Mapping(source = "product.title", target ="productName")
    @Mapping(source = "product.imgUrl", target = "productImageUrl")
    OrderItemResponse toItemResponse(OrderItem item);

    List<OrderItemResponse> toItemResponseList(List<OrderItem> items);
}
