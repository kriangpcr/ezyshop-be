package com.example.ezyshop.service;

import com.example.ezyshop.dto.order.OrderItemResponse;
import com.example.ezyshop.dto.order.OrderRequest;
import com.example.ezyshop.dto.order.OrderResponse;
import com.example.ezyshop.entity.*;
import com.example.ezyshop.enums.OrderStatus;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.mapper.OrderMapper;
import com.example.ezyshop.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public void createOrder(
            OrderRequest request,
            Authentication authentication
    ) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        UserAddress address = userAddressRepository.findById(request.getAddress_id())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot use this address");
        }

        Order order = new Order();
        order.setUser(user);
        order.setUserAddress(address);
        order.setStatus(OrderStatus.PENDING);

        for (var s : request.getItems()) {

            Product product = productRepository.findById(s.getProduct_id())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            if (product.getStock() < s.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock");
            }

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(s.getQuantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(s.getQuantity());
            orderItem.setTotalPrice(itemTotal);
            orderItem.setOrder(order);

            order.getItems().add(orderItem);

            totalPrice = totalPrice.add(itemTotal);
        }

        if (user.getBalance().compareTo(totalPrice) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        for (var orderItem : order.getItems()) {
            Product product = orderItem.getProduct();

            product.setStock(
                    product.getStock() - orderItem.getQuantity()
            );

            productRepository.save(product);
        }

        user.setBalance(
                user.getBalance().subtract(totalPrice)
        );
        userRepository.save(user);

        order.setTotalPrice(totalPrice);

        orderRepository.save(order);

        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Cart not found"));

        cartItemRepository.deleteCartItemsByCart(cart);

    }

    public List<OrderResponse> getOrders(Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Order> orders = orderRepository.findAllByUserId(user.getId());

        return orderMapper.toResponseList(orders);
    }
}
