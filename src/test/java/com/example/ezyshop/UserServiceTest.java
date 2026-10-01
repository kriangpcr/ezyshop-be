package com.example.ezyshop;

import com.example.ezyshop.dto.user.UserResponse;
import com.example.ezyshop.entity.User;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.mapper.UserMapper;
import com.example.ezyshop.repository.UserRepository;
import com.example.ezyshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;
    @Test
    void shouldGetUserSuccessfully() {

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("test");

        User user = new User();
        user.setId("user-1");
        user.setUsername("test");

        UserResponse userResponse = new UserResponse();

        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(user));

        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        UserResponse response =
                userService.getUser(authentication);

        assertEquals(userResponse, response);

        verify(userRepository)
                .findByUsername("test");

        verify(userMapper)
                .toResponse(user);

    }

    @Test
    void shouldAddBalanceSuccessfully() {

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("test");

        User user = new User();
        user.setId("user-1");
        user.setUsername("test");

        BigDecimal balance = new BigDecimal("1000.00");

        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        userService.addBalance(balance, authentication);

        assertEquals(balance, user.getBalance());

        verify(userRepository)
                .findByUsername("test");

        verify(userRepository)
                .save(user);
    }
}
