package com.example.ezyshop.repository;

import com.example.ezyshop.entity.Product;
import com.example.ezyshop.entity.ProductItem;
import com.example.ezyshop.enums.ProductItemStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductItemRepository extends JpaRepository<ProductItem,String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProductItem> findFirstByProductAndStatus(
            Product product,
            ProductItemStatus status
    );
    @Query("""
    SELECT COUNT(p)
    FROM ProductItem p
    WHERE p.product = :product AND p.status = 'AVAILABLE'
""")

    long countByProductAndAvailable(@Param("product") Product product);
}
