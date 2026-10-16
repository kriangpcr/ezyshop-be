package com.example.ezyshop.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest {

    @NotBlank(message = "VoucherCode ID is required")
    @Size(min = 35, max = 35, message = "Voucher code must be exactly 35 characters")
    private String voucherCode;
}
