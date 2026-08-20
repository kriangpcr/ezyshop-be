package com.example.ezyshop.service;

import com.example.ezyshop.dto.cart.CartRequest;
import com.example.ezyshop.dto.cart.CartResponse;
import com.example.ezyshop.entity.Cart;
import com.example.ezyshop.entity.CartItem;
import com.example.ezyshop.entity.Product;
import com.example.ezyshop.entity.User;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.mapper.CartMapper;
import com.example.ezyshop.repository.CartItemRepository;
import com.example.ezyshop.repository.CartRepository;
import com.example.ezyshop.repository.ProductRepository;
import com.example.ezyshop.repository.UserRepository;
import com.example.ezyshop.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartServcie {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartResponse getCart(Authentication authentication){
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        if (userDetails == null){
            throw new ResourceNotFoundException("User not found");
        }
        Cart cart = cartRepository.findCartWithItemsAndProducts(userDetails.getId()).orElseThrow(()->new ResourceNotFoundException("Cart not found"));
        return cartMapper.toResponse(cart);
    }

    public void addCart(CartRequest request, Authentication authentication){

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow( ()->
                new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProduct_id()).orElseThrow( ()->
                new ResourceNotFoundException("Product not found"));;

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity()
            );
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());

            cart.getItems().add(cartItem);
        }
        cartRepository.save(cart);

    }
    public void plusCart(String id, Authentication authentication) {
        CartItem item = getCartItemForUser(id, authentication);

        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.save(item);
    }

    public void minusCart(String id, Authentication authentication) {
        CartItem item = getCartItemForUser(id, authentication);

        if (item.getQuantity() <= 1) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepository.save(item);
        }
    }

    public void deleteCart(String id, Authentication authentication) {
        CartItem item = getCartItemForUser(id, authentication);

        cartItemRepository.delete(item);
    }

    private CartItem getCartItemForUser(
            String id,
            Authentication authentication
    ) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CartItem not found"));

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        if (!item.getCart().getUser().getId().equals(userDetails.getId())) {
            throw new AccessDeniedException(
                    "You cannot access this CartItem"
            );
        }

        return item;
    }
}
