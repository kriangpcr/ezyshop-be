package com.example.ezyshop.repository;

import com.example.ezyshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository  extends JpaRepository<Cart,String> {
    @Query("""
    SELECT DISTINCT c
    FROM Cart c
    LEFT JOIN FETCH c.items ci
    LEFT JOIN FETCH ci.product p
    WHERE c.user.id = :userId
""")
    Optional<Cart> findCartWithItemsAndProducts(
            @Param("userId") String userId
    );

    Optional<Cart> findByUserId(String userId);
}
