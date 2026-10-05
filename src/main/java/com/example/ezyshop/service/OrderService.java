package com.example.ezyshop.service;

import com.example.ezyshop.dto.order.CreateOrderRequest;
import com.example.ezyshop.dto.order.OrderItemResponse;
import com.example.ezyshop.dto.order.OrderResponse;
import com.example.ezyshop.entity.*;
import com.example.ezyshop.enums.OrderStatus;
import com.example.ezyshop.enums.ProductItemStatus;
import com.example.ezyshop.exception.BadRequestException;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.repository.*;
import com.example.ezyshop.security.CustomUserDetails;
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
    private final PaymentHistoryRepository paymentHistoryRepository;

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
        BigDecimal balanceBefore = user.getBalance();
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
        BigDecimal balanceAfter =
                balanceBefore.subtract(totalPrice);

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setBalanceBefore(balanceBefore);
        paymentHistory.setAmount(totalPrice);
        paymentHistory.setBalanceAfter(balanceAfter);
        paymentHistory.setOrder(order);
        paymentHistory.setUser(user);
        paymentHistoryRepository.save(paymentHistory);

        product.setSold(product.getSold()+request.getQuantity());
        user.setBalance(balanceAfter);
        orderItemRepository.saveAll(orderItems);
    }

    public List<OrderResponse> getOrders(Authentication authentication){

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        if (user == null){
            throw new ResourceNotFoundException("User not found");
        }
        List<Order> orders = orderRepository.findAllByUserId(user.getId());

        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders){
            OrderResponse orderResponse = new OrderResponse();

            orderResponse.setStatus(order.getStatus());
            orderResponse.setCategory(order.getProduct().getCategory());
            orderResponse.setQuantity(order.getQuantity());
            orderResponse.setTitle(order.getProduct().getTitle());
            orderResponse.setOrderId(order.getId());

            List<OrderItemResponse> orderItems = new ArrayList<>();
            for (OrderItem item: order.getOrderItems()){
                OrderItemResponse orderItemResponse = new OrderItemResponse();
                orderItemResponse.setData(item.getProductItem().getData());
                orderItems.add(orderItemResponse);
            }
            orderResponse.setItems(orderItems);
            responses.add(orderResponse);
        }
        return responses;
    }
}
