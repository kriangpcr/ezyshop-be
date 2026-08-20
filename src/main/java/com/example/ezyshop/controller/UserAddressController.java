package com.example.ezyshop.controller;

import com.example.ezyshop.dto.useraddress.UserAddressRequest;
import com.example.ezyshop.dto.useraddress.UserAddressResponse;
import com.example.ezyshop.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
public class UserAddressController {
    private final UserAddressService userAddressService;

    @PostMapping("/create")
    public ResponseEntity<UserAddressResponse>  createAddress(@RequestBody UserAddressRequest request, Authentication authentication){
        UserAddressResponse response = userAddressService.createAddress(request,authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<UserAddressResponse>>  getAddresses(Authentication authentication){
        List<UserAddressResponse> response = userAddressService.getAddress(authentication);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void>  deleteAddress(@PathVariable String id,Authentication authentication){
        userAddressService.deleteAddress(id,authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserAddressResponse> updateAddress(
            @PathVariable String id,
            @RequestBody UserAddressRequest request,
            Authentication authentication
    ) {
        UserAddressResponse response =
                userAddressService.updateAddress(
                        id,
                        request,
                        authentication
                );

        return ResponseEntity.ok(response);
    }
}
