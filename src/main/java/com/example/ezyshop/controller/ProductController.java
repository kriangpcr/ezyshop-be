package com.example.ezyshop.controller;

import com.example.ezyshop.dto.product.ProductRequest;
import com.example.ezyshop.dto.product.ProductResponse;
import com.example.ezyshop.security.CustomUserDetails;
import com.example.ezyshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("/get")
    public ResponseEntity<Page<ProductResponse>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getAllProduct(page, size));
    }

}
