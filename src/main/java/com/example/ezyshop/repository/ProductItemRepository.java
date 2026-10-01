package com.example.ezyshop.repository;

import com.example.ezyshop.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem,String> {
}
