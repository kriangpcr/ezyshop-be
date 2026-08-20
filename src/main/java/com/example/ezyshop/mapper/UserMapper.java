package com.example.ezyshop.mapper;

import com.example.ezyshop.dto.user.UserResponse;
import com.example.ezyshop.dto.useraddress.UserAddressRequest;
import com.example.ezyshop.dto.useraddress.UserAddressResponse;
import com.example.ezyshop.entity.User;
import com.example.ezyshop.entity.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserAddress toEntity(UserAddressRequest request);

    UserAddressResponse toResponse(UserAddress request);

    UserResponse toResponse(User request);

    void updateEntity(UserAddressRequest request, @MappingTarget UserAddress address);
}
