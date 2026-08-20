package com.example.ezyshop.dto.useraddress;

import com.example.ezyshop.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserAddressRequest {

    private String address;

    private String subdistrict;

    private String district;

    private String province;

    private String postal_code;

    private String country;

}
