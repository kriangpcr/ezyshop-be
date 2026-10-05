package com.example.ezyshop.repository;

import com.example.ezyshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {
//    List<Order> findAllByUserId(String userId);
    @Query("""
    SELECT DISTINCT o
        FROM Order o
            JOIN FETCH o.product
                JOIN FETCH o.orderItems oi
                    JOIN FETCH oi.productItem
                        WHERE o.user.id = :userId
                            ORDER BY o.createdAt DESC
               
    """)
    List<Order> findAllByUserId( @Param("userId") String userId);
}
