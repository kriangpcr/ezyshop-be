package com.example.ezyshop.repository;

import com.example.ezyshop.entity.Cart;
import com.example.ezyshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,String> {
    Integer deleteCartItemsByCart(Cart cart);
}
