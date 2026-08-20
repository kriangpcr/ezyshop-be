package com.example.ezyshop.service;

import com.example.ezyshop.dto.product.ProductRequest;
import com.example.ezyshop.dto.product.ProductResponse;
import com.example.ezyshop.entity.Product;
import com.example.ezyshop.entity.User;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.mapper.ProductMapper;
import com.example.ezyshop.repository.ProductRepository;
import com.example.ezyshop.repository.UserRepository;
import com.example.ezyshop.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SupabaseStorageService supabaseStorageService;

    public ProductResponse createProduct(
            ProductRequest request,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User seller = userRepository.findById(userDetails.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        String fileName = null;
        try {
            fileName = supabaseStorageService.upload(request.getImage());

            Product product = productMapper.toEntity(request);
            product.setSeller(seller);
            product.setSold(0);
            product.setImgUrl(supabaseStorageService.getPublicUrl(fileName));

            Product savedProduct = productRepository.save(product);

            return productMapper.toResponse(savedProduct);

        } catch (Exception e) {

            if (fileName != null) {
                try {
                    supabaseStorageService.delete(fileName);
                } catch (Exception deleteException) {
                    // log ไว้ ไม่ควรกลบ exception ตัวแรก
                }
            }

            throw e;
        }
    }

    public ProductResponse getProduct(String id){
        Product product = productRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product not found"));
        return productMapper.toResponse(product);
    }

    public Page<ProductResponse> getAllProduct(int page, int size) {

        Page<Product> product =
                productRepository.findAllWithSellerAndImages(
                        PageRequest.of(page, size)
                );

        return product.map(product1 -> productMapper.toResponse(product1));
    }
}
