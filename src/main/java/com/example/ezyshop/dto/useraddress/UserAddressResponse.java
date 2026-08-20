package com.example.ezyshop.dto.useraddress;

import com.example.ezyshop.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class UserAddressResponse {

    private String id;

    private String address;

    private String subdistrict;

    private String district;

    private String province;

    private String postal_code;

    private String country;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
