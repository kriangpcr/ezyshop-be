package com.example.ezyshop;

import com.example.ezyshop.dto.order.OrderItemRequest;
import com.example.ezyshop.dto.order.OrderRequest;
import com.example.ezyshop.dto.product.ProductRequest;
import com.example.ezyshop.dto.product.ProductResponse;
import com.example.ezyshop.entity.*;
import com.example.ezyshop.enums.Role;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.mapper.ProductMapper;
import com.example.ezyshop.repository.ProductRepository;
import com.example.ezyshop.repository.UserRepository;
import com.example.ezyshop.security.CustomUserDetails;
import com.example.ezyshop.service.ProductService;
import com.example.ezyshop.service.SupabaseStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  ProductRepository productRepository;
    @Mock
    private  ProductMapper productMapper;
    @Mock
    private  SupabaseStorageService supabaseStorageService;
    @InjectMocks
    private ProductService productService;
    @Test
    void shouldCreateProductSuccessfully(){

        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        MultipartFile image = mock(MultipartFile.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn("user-1");

        User user = new User();
        user.setId("user-1");
        user.setUsername("test");

        Product product = new Product();
        product.setId("product-id");
        product.setSeller(user);

        ProductRequest request= new ProductRequest();
        request.setTitle("product-name");
        request.setImage(image);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId("product-id");
        productResponse.setUser_id("user-1");
        productResponse.setImgUrl("https://example.com/image.jpg");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toEntity(request)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);
        when(supabaseStorageService.upload(any(MultipartFile.class)))
                .thenReturn("image.jpg");

        when(supabaseStorageService.getPublicUrl("image.jpg"))
                .thenReturn("https://example.com/image.jpg");

        ProductResponse response = productService.createProduct(request,authentication);

        assertEquals("product-id",response.getId());
        assertEquals("user-1",response.getUser_id());
        assertEquals("https://example.com/image.jpg",response.getImgUrl());

        verify(userRepository).findById("user-1");
        verify(supabaseStorageService).upload(image);
        verify(supabaseStorageService).getPublicUrl("image.jpg");
        verify(productRepository).save(product);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){

        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn("user-1");

        User user = new User();
        user.setId("user-1");
        user.setUsername("test");

        ProductRequest request= new ProductRequest();

        when(userRepository.findById("user-1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception =  assertThrows(
                ResourceNotFoundException.class,
                () -> productService.createProduct(
                        request,
                        authentication
                )
        );

        assertEquals("User not found",exception.getMessage());
        verify(userRepository).findById("user-1");
    }
    @Test
    void shouldGetProductSuccessfully() {

        Product product = new Product();
        product.setId("product-1");

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId("product-1");

        when(productRepository.findById("product-1"))
                .thenReturn(Optional.of(product));

        when(productMapper.toResponse(product))
                .thenReturn(productResponse);

        ProductResponse response =
                productService.getProduct("product-1");

        assertEquals("product-1", response.getId());

        verify(productRepository).findById("product-1");
        verify(productMapper).toResponse(product);
    }
    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        when(productRepository.findById("product-1"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProduct("product-1")
        );

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository).findById("product-1");

        verify(productMapper, never()).toResponse(any(Product.class));
    }
    @Test
    void shouldGetAllProductsSuccessfully() {

        Product product1 = new Product();
        product1.setId("product-1");

        Product product2 = new Product();
        product2.setId("product-2");

        ProductResponse response1 = new ProductResponse();
        response1.setId("product-1");

        ProductResponse response2 = new ProductResponse();
        response2.setId("product-2");

        Page<Product> productPage =
                new PageImpl<>(List.of(product1, product2));

        when(productRepository.findAllWithSellerAndImages(
                PageRequest.of(0, 10)
        )).thenReturn(productPage);

        when(productMapper.toResponse(product1))
                .thenReturn(response1);

        when(productMapper.toResponse(product2))
                .thenReturn(response2);

        Page<ProductResponse> response =
                productService.getAllProduct(0, 10);

        assertEquals(2, response.getTotalElements());
        assertEquals("product-1", response.getContent().get(0).getId());
        assertEquals("product-2", response.getContent().get(1).getId());

        verify(productRepository)
                .findAllWithSellerAndImages(PageRequest.of(0, 10));

        verify(productMapper).toResponse(product1);
        verify(productMapper).toResponse(product2);
    }
}
