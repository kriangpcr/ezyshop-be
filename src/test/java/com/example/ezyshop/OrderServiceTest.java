package com.example.ezyshop;

import com.example.ezyshop.dto.order.OrderItemRequest;
import com.example.ezyshop.dto.order.OrderRequest;
import com.example.ezyshop.dto.order.OrderResponse;
import com.example.ezyshop.entity.*;
import com.example.ezyshop.enums.Role;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.mapper.OrderMapper;
import com.example.ezyshop.repository.*;
import com.example.ezyshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  UserAddressRepository userAddressRepository;
    @Mock
    private  ProductRepository productRepository;
    @Mock
    private  OrderItemRepository orderItemRepository;
    @Mock
    private  OrderRepository orderRepository;
    @Mock
    private  CartRepository cartRepository;
    @Mock
    private  CartItemRepository cartItemRepository;
    @Mock
    private  OrderMapper orderMapper;
    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully(){
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("test");

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId("user-1");
        user.setUsername("test");
        user.setBalance(new BigDecimal("2000"));
        user.setRole(Role.USER);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        UserAddress address = new UserAddress();
        address.setId("address-1");
        address.setUser(user);

        Product product = new Product();
        product.setId("product-1");
        product.setPrice(new BigDecimal("500"));
        product.setStock(10);

        Cart cart = new Cart();
        cart.setUser(user);

        OrderRequest request = new OrderRequest();

        request.setAddress_id("address-1");

        OrderItemRequest item = new OrderItemRequest();
        item.setProduct_id("product-1");
        item.setQuantity(2);

        request.setItems(List.of(item));


        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(user));

        when(userAddressRepository.findById("address-1"))
                .thenReturn(Optional.of(address));

        when(productRepository.findById("product-1"))
                .thenReturn(Optional.of(product));

        when(cartRepository.findByUserId("user-1"))
                .thenReturn(Optional.of(cart));

        orderService.createOrder(request, authentication);

        assertEquals(
                new BigDecimal("1000"),
                user.getBalance()
        );

        assertEquals(
                8,
                product.getStock()
        );

        verify(userRepository).save(user);

        verify(productRepository).save(product);

        verify(orderRepository).save(any(Order.class));

        verify(cartItemRepository)
                .deleteCartItemsByCart(cart);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("test");

        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.empty());

        OrderRequest request = new OrderRequest();

        // Act + Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> orderService.createOrder(
                                request,
                                authentication
                        )
                );

        assertEquals(
                "User not found",
                exception.getMessage()
        );
        // Verify
        verify(userRepository)
                .findByUsername("test");
    }

    @Test
    void shouldGetOrders(){
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("test");

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId("user-1");
        user.setUsername("test");
        user.setBalance(new BigDecimal("2000"));
        user.setRole(Role.USER);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        Order order = new Order();
        order.setId("order-1");
        order.setUser(user);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId("order-1");

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(orderRepository.findAllByUserId("user-1")).thenReturn(List.of(order));
        when(orderMapper.toResponseList(List.of(order)))
                .thenReturn(List.of(orderResponse));

        List<OrderResponse> response = orderService.getOrders(authentication);

        assertEquals("order-1", response.getFirst().getId());

        verify(userRepository).findByUsername("test");

        verify(orderRepository).findAllByUserId("user-1");

        verify(orderMapper)
                .toResponseList(List.of(order));
    }
}
