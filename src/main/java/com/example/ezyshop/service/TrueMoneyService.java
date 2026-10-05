package com.example.ezyshop.service;

import com.example.ezyshop.dto.payment.TrueMoney.TrueMoneyRequest;
import com.example.ezyshop.dto.payment.TrueMoney.TrueMoneyData;
import com.example.ezyshop.dto.payment.TrueMoney.TrueMoneyResponse;
import com.example.ezyshop.dto.payment.TrueMoney.TrueMoneyStatus;
import com.example.ezyshop.dto.payment.TrueMoney.Voucher;
import com.example.ezyshop.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TrueMoneyService {

    private final RestClient restClient;

    @Value("${truewallet.mobile}")
    private String mobile;

    @Value("${truewallet.mock-success-amount:200.00}")
    private BigDecimal mockSuccessAmount;

    @Value("${truewallet.mock:true}")
    private boolean mock;

    public TrueMoneyResponse redeem(
            String voucherCode
    ) {

        if (mock) {
            TrueMoneyStatus status = new TrueMoneyStatus("Voucher redeemed successfully.", "SUCCESS");
            Voucher voucher = new Voucher();
            voucher.setVoucherId(voucherCode);
            voucher.setAmountBaht(mockSuccessAmount);
            voucher.setRedeemedAmountBaht(mockSuccessAmount);
            voucher.setStatus("redeemed");

            TrueMoneyData data = new TrueMoneyData();
            data.setVoucher(voucher);

            return new TrueMoneyResponse(status, data);
        }

        String url =
                "https://gift.truemoney.com/campaign/vouchers/"
                        + voucherCode
                        + "/redeem";

        TrueMoneyRequest request = new TrueMoneyRequest();
        request.setVoucherCode(voucherCode);
        request.setMobile(mobile);
        try {
            return restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(TrueMoneyResponse.class);

        } catch (HttpClientErrorException e) {


            String responseBody = e.getResponseBodyAsString();

            try {
                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode root = objectMapper.readTree(responseBody);

                String message = root
                        .path("status")
                        .path("message")
                        .asText("TrueMoney voucher is invalid.");

                throw new BadRequestException(message);

            } catch (JsonProcessingException ex) {
                throw new BadRequestException("TrueMoney voucher is invalid.");
            }
        }
    }
}
