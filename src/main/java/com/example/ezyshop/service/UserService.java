package com.example.ezyshop.service;

import com.example.ezyshop.dto.user.UserResponse;
import com.example.ezyshop.entity.User;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.mapper.UserMapper;
import com.example.ezyshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUser(Authentication authentication){

        User user = userRepository.findByUsername(authentication.getName())  .orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }
}
