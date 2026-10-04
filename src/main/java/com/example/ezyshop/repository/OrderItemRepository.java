package com.example.ezyshop.repository;

import com.example.ezyshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,String> {
    List<OrderItem> findAllByOrderId(String order_id);
}
