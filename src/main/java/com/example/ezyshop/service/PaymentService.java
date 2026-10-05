package com.example.ezyshop.service;

import com.example.ezyshop.dto.payment.DepositRequest;
import com.example.ezyshop.dto.payment.PaymentResponse;
import com.example.ezyshop.dto.payment.TrueMoney.TrueMoneyResponse;
import com.example.ezyshop.entity.Payment;
import com.example.ezyshop.entity.User;
import com.example.ezyshop.enums.payment.PaymentMethod;
import com.example.ezyshop.enums.payment.PaymentStatus;
import com.example.ezyshop.enums.payment.PaymentType;
import com.example.ezyshop.exception.BadRequestException;
import com.example.ezyshop.exception.ResourceNotFoundException;
import com.example.ezyshop.repository.PaymentRepository;
import com.example.ezyshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final TrueMoneyService trueMoneyService;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentResponse deposit(DepositRequest request, Authentication authentication){

        if (paymentRepository.existsByVoucherCode(request.getVoucherCode())) {
            throw new BadRequestException(
                    "Voucher has already been used"
            );
        }
        TrueMoneyResponse trueMoneyResponse = trueMoneyService.redeem(request.getVoucherCode());

        if (!trueMoneyResponse.getStatus().getCode().equals("SUCCESS")){
            throw new BadRequestException(
                    trueMoneyResponse.getStatus().getMessage()
            );
        }

        BigDecimal amount = trueMoneyResponse.getData().getVoucher().getAmountBaht();

        User user = userRepository.findByUsernameForUpdate(authentication.getName()).orElseThrow(() ->
                new ResourceNotFoundException(
                        "User not found"
                ));

        user.setBalance(
                user.getBalance().add(amount)
        );
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentMethod(PaymentMethod.TRUE_MONEY_ANGPAO);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setType(PaymentType.DEPOSIT);
        payment.setUser(user);
        payment.setVoucherCode(request.getVoucherCode());

        paymentRepository.save(payment);

        PaymentResponse response = new PaymentResponse();
        response.setAmount(amount);
        response.setStatus("SUCCESS");

        return response;
    }
}
