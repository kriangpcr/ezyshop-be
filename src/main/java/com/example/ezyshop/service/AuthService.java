package com.example.ezyshop.service;

import com.example.ezyshop.dto.auth.*;
import com.example.ezyshop.entity.User;
import com.example.ezyshop.enums.Role;
import com.example.ezyshop.exception.BadRequestException;
import com.example.ezyshop.mapper.ProductMapper;
import com.example.ezyshop.mapper.UserMapper;
import com.example.ezyshop.repository.UserRepository;
import com.example.ezyshop.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public void register(RegisterRequest request){
        boolean existUser = userRepository.existsByUsername(request.getUsername());
        if (existUser){
            throw new BadRequestException("Username already Exist");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(new BigDecimal(0));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String username = userDetails.getUsername();

        String accessToken  = jwtService.generateToken(username);
        String refreshToken  = jwtService.generateRefreshToken(username);
        User user = userRepository.findByUsername(username).orElseThrow();

        LoginResponse response = new LoginResponse();

        response.setUser(userMapper.toResponse(user));
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }
        String username = jwtService.extractUsername(refreshToken);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String accessToken  = jwtService.generateToken(username);
        String newRefreshToken  = jwtService.generateRefreshToken(username);

        RefreshTokenResponse response = new RefreshTokenResponse();

        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);
        return response;
    }
}
