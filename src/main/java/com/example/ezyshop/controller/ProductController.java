package com.example.ezyshop.controller;

import com.example.ezyshop.dto.product.ProductItemRequest;
import com.example.ezyshop.dto.product.ProductRequest;
import com.example.ezyshop.dto.product.ProductResponse;
import com.example.ezyshop.security.CustomUserDetails;
import com.example.ezyshop.service.ProductItemService;
import com.example.ezyshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ProductItemService productItemService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductRequest request,
            Authentication authentication){
        return ResponseEntity.ok(productService.createProduct(request, authentication));
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<?> getProduct(
            @PathVariable String id
    ){
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping(path = "/get")
    public ResponseEntity<Page<ProductResponse>> getProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getProducts(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/items/create")
    public ResponseEntity<String> createProductItems(
            @Valid @RequestBody ProductItemRequest request,
            Authentication authentication){
        return ResponseEntity.ok(productItemService.createItems(request));
    }



}
