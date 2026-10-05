package com.example.ezyshop.dto.payment.TrueMoney;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrueMoneyData {
    private Voucher voucher;

    @JsonProperty("owner_profile")
    private OwnerProfile ownerProfile;

    @JsonProperty("redeemer_profile")
    private RedeemerProfile redeemerProfile;

    @JsonProperty("my_ticket")
    private Object myTicket;

    private List<Object> tickets;
}
