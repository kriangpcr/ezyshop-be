package com.example.ezyshop.service;

import com.example.ezyshop.dto.useraddress.UserAddressRequest;
import com.example.ezyshop.dto.useraddress.UserAddressResponse;
import com.example.ezyshop.entity.User;
import com.example.ezyshop.entity.UserAddress;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.mapper.UserMapper;
import com.example.ezyshop.repository.UserAddressRepository;
import com.example.ezyshop.repository.UserRepository;
import com.example.ezyshop.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressService {
    private final UserAddressRepository userAddressRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserAddressResponse createAddress(UserAddressRequest request, Authentication authentication){

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
        UserAddress userAddress = userMapper.toEntity(request);
        userAddress.setUser(user);
        userAddressRepository.save(userAddress);
        return userMapper.toResponse(userAddress);

    }

    public List<UserAddressResponse> getAddress(Authentication authentication){

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        if (userDetails == null){
            throw new ResourceNotFoundException("User not found");
        }
        List<UserAddress> userAddresses = userAddressRepository.findByUserId(userDetails.getId());

        return userAddresses.stream().map(userMapper::toResponse).toList();

    }

    public void deleteAddress(String id,Authentication authentication) {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        UserAddress address = userAddressRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!userDetails.getId().equals(address.getUser().getId())){
            throw new AccessDeniedException("You cannot delete this address");
        }

        userAddressRepository.deleteById(id);

    }
    @Transactional
    public UserAddressResponse updateAddress(String id,UserAddressRequest request,Authentication authentication) {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        UserAddress address = userAddressRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!userDetails.getId().equals(address.getUser().getId())){
            throw new AccessDeniedException("You cannot update this address");
        }

        userMapper.updateEntity(request,address);

        return userMapper.toResponse(address);

    }


}
