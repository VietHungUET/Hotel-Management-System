package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for payment response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Integer paymentId;
    private Integer bookingId;
    private Double amount;
    private String paymentDate;
    private String paymentMethod;
}
