package com.example.ezyshop.repository;

import com.example.ezyshop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,String> {
    boolean existsByVoucherCode(String voucherCode);
}
