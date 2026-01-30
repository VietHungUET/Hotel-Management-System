package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.PaymentEntity;

import java.util.List;

/**
 * Interface for payment service operations.
 */
public interface IPaymentService {

    // CRUD operations
    PaymentEntity getPaymentById(int id);

    List<PaymentEntity> getAllPayments();

    PaymentEntity savePayment(PaymentEntity payment);

    // Query operations
    List<PaymentEntity> getByDate(String paymentDate);

    List<PaymentEntity> getByYear(String year);

    // Business logic
    List<PaymentEntity> processPaymentsFromBookings(List<String> bookingIds);

    // DTO conversion
    PaymentDto convertToDto(PaymentEntity payment);

    List<PaymentDto> getConvertedPayments(List<PaymentEntity> payments);

    List<Double> getRevenueByYear(String year);
}
