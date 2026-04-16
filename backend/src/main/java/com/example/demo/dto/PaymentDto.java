package com.example.demo.dto;

/**
 * DTO for payment response.
 */
public record PaymentDto(
        Integer paymentId,
        Integer bookingId,
        Double amount,
        String paymentDate,
        String paymentMethod
) {}
