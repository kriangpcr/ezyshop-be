package com.example.ezyshop.repository;

import com.example.ezyshop.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory,String> {
}
