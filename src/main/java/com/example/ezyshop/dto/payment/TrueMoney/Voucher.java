package com.example.ezyshop.dto.payment.TrueMoney;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @JsonProperty("voucher_id")
    private String voucherId;

    @JsonProperty("amount_baht")
    private BigDecimal amountBaht;

    @JsonProperty("redeemed_amount_baht")
    private BigDecimal redeemedAmountBaht;

    private Integer member;

    private String status;

    private String link;

    private String detail;

    @JsonProperty("expire_date")
    private Long expireDate;

    private String type;

    private Integer redeemed;

    private Integer available;
}
