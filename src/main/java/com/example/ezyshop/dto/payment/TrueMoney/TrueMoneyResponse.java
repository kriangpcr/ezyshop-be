package com.example.ezyshop.dto.payment.TrueMoney;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrueMoneyResponse {
    private TrueMoneyStatus status;
    private TrueMoneyData data;
}
