package com.example.ezyshop.dto.payment.TrueMoney;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedeemerProfile {
    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("tmn_id")
    private String tmnId;
}
