package com.example.ezyshop.service;

import com.example.ezyshop.dto.product.ProductItemRequest;
import com.example.ezyshop.entity.Product;
import com.example.ezyshop.entity.ProductItem;
import com.example.ezyshop.enums.ProductItemStatus;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.repository.ProductItemRepository;
import com.example.ezyshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;

    public String createItems(ProductItemRequest request){
        Product product = productRepository.findById(request.getProduct_id()).orElseThrow(()->
                new ResourceNotFoundException("Product not found"));

        List<ProductItem> productItems = request.getData().stream().map(data -> {
            ProductItem productItem = new ProductItem();

            productItem.setData(data);
            productItem.setStatus(ProductItemStatus.AVAILABLE);
            productItem.setProduct(product);

            return productItem;
        }).toList();

        productItemRepository.saveAll(productItems);

        return "Success";

    }
}
