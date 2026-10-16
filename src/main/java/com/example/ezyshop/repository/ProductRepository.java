package com.example.ezyshop.repository;

import com.example.ezyshop.dto.product.ProductResponse;
import com.example.ezyshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    @Query("""
    SELECT new com.example.ezyshop.dto.product.ProductResponse(
        p.id,
        p.title,
        p.description,
        p.price,
        p.category,
        p.imgUrl,
        p.sold,
         (SELECT COUNT(pi.id)
                  FROM ProductItem pi
                  WHERE pi.product = p
                  AND pi.status = com.example.ezyshop.enums.ProductItemStatus.AVAILABLE),
        p.createdAt,
        p.updatedAt
    )
    FROM Product p
""")
    Page<ProductResponse> findAllWithStock(Pageable pageable);

    @Query("""
    SELECT new com.example.ezyshop.dto.product.ProductResponse(
        p.id,
        p.title,
        p.description,
        p.price,
        p.category,
        p.imgUrl,
        p.sold,
         (SELECT COUNT(pi.id)
                  FROM ProductItem pi
                  WHERE pi.product = p
                  AND pi.status = com.example.ezyshop.enums.ProductItemStatus.AVAILABLE),
        p.createdAt,
        p.updatedAt
    )
    FROM Product p
""")
    List<ProductResponse> findAllProduct();
}
