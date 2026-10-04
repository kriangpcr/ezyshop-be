package com.example.ezyshop.repository;

import com.example.ezyshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {
    List<Order> findAllByUserId(String userId);
}
