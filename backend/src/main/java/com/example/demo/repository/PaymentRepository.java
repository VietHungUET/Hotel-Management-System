package com.example.demo.repository;

import com.example.demo.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {

    @Query("SELECT p FROM PaymentEntity p where p.paymentDate = :paymentDate")
    List<PaymentEntity> findByPaymentDate(String paymentDate);

    @Query("SELECT p FROM PaymentEntity p WHERE p.paymentDate LIKE CONCAT('%', :year, '%')")
    List<PaymentEntity> findByPaymentYear(String year);
}
