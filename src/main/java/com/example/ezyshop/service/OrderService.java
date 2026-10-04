package com.example.ezyshop.service;

import com.example.ezyshop.dto.order.CreateOrderRequest;
import com.example.ezyshop.dto.order.OrderItemResponse;
import com.example.ezyshop.entity.*;
import com.example.ezyshop.enums.OrderStatus;
import com.example.ezyshop.enums.ProductItemStatus;
import com.example.ezyshop.exception.BadRequestException;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(CreateOrderRequest request, Authentication authentication){

        Product product = productRepository.findById(request.getProduct_id()).orElseThrow(()->
                new ResourceNotFoundException("Product not found"));

        long countProductItem = productItemRepository.countByProductAndAvailable(product);

        if (countProductItem < request.getQuantity()){
            throw new BadRequestException("Item not enough");
        }

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(()->
                new ResourceNotFoundException("User not found"));

        BigDecimal totalPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));
        if (user.getBalance().compareTo(totalPrice) < 0){
            throw new BadRequestException("Your balance not enough");
        }

        Order order = new Order();
        order.setUser(user);
        order.setQuantity(request.getQuantity());
        order.setProduct(product);
        order.setStatus(OrderStatus.SUCCESS);
        orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < request.getQuantity(); i++) {
            ProductItem productItem = productItemRepository.findFirstByProductAndStatus(product,ProductItemStatus.AVAILABLE).orElseThrow(()->
                    new ResourceNotFoundException("Item not found"));
            productItem.setStatus(ProductItemStatus.SOLD);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductItem(productItem);
            orderItems.add(orderItem);
        }
        product.setSold(product.getSold()+request.getQuantity());
        user.setBalance(user.getBalance().subtract(totalPrice));
        orderItemRepository.saveAll(orderItems);
    }

    public List<OrderItemResponse> getOrders(Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(()->
                new ResourceNotFoundException("User not found"));
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        List<OrderItemResponse> responses = new ArrayList<>();
        for (Order order : orders){
            List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());
            for (OrderItem item : orderItems){

                OrderItemResponse i = new OrderItemResponse();
                i.setCategory(order.getProduct().getCategory());
                i.setData(item.getProductItem().getData());
                i.setTitle(order.getProduct().getTitle());
                responses.add(i);
            }
        }
        return responses;
    }
}
