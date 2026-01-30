package com.example.demo.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.PaymentEntity;
import com.example.demo.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final IPaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPayments() {
        List<PaymentEntity> payments = paymentService.getAllPayments();
        List<PaymentDto> paymentDtos = paymentService.getConvertedPayments(payments);
        return ResponseEntity.ok(new ApiResponse("Payments retrieved successfully", paymentDtos));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addPayment(@RequestBody PaymentEntity payment) {
        PaymentEntity savedPayment = paymentService.savePayment(payment);
        PaymentDto paymentDto = paymentService.convertToDto(savedPayment);
        return ResponseEntity.ok(new ApiResponse("Payment added successfully", paymentDto));
    }

    @GetMapping("/revenue/{year}")
    public ResponseEntity<ApiResponse> getRevenue(@PathVariable String year) {
        List<Double> revenue = paymentService.getRevenueByYear(year);
        return ResponseEntity.ok(new ApiResponse("Revenue calculated successfully", revenue));
    }
}
