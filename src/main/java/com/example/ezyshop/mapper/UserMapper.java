package com.example.ezyshop.mapper;

import com.example.ezyshop.dto.user.UserResponse;
import com.example.ezyshop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User request);
}
