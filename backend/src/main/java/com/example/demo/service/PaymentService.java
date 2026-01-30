package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.BookingEntity;
import com.example.demo.entity.PaymentEntity;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public PaymentEntity getPaymentById(int id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", id));
    }

    @Override
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentEntity savePayment(PaymentEntity payment) {
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public List<PaymentEntity> getByDate(String paymentDate) {
        return paymentRepository.findByPaymentDate(paymentDate);
    }

    @Override
    public List<PaymentEntity> getByYear(String year) {
        return paymentRepository.findByPaymentYear(year);
    }

    @Override
    public List<PaymentEntity> processPaymentsFromBookings(List<String> bookingIds) {
        List<PaymentEntity> payments = new ArrayList<>();

        for (String bookingIdStr : bookingIds) {
            String[] parts = bookingIdStr.split("_");
            Integer bookingId = Integer.valueOf(parts[0]);
            String paymentMethod = parts[1];

            BookingEntity booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId));

            PaymentEntity payment = new PaymentEntity();
            payment.setBookingId(bookingId);
            payment.setAmount(booking.getMoney());
            payment.setPaymentMethod(paymentMethod);

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            payment.setPaymentDate(today.format(formatter));

            payments.add(payment);
        }

        // Save all payments
        for (PaymentEntity payment : payments) {
            paymentRepository.save(payment);
        }

        return payments;
    }

    @Override
    public List<Double> getRevenueByYear(String year) {
        List<Double> monthlyRevenue = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String month = String.format("%s-%02d", year, i);
            List<PaymentEntity> monthlyPayments = paymentRepository.findByPaymentYear(month);
            double sum = monthlyPayments.stream()
                    .mapToDouble(PaymentEntity::getAmount)
                    .sum();
            monthlyRevenue.add(sum);
        }
        return monthlyRevenue;
    }

    @Override
    public PaymentDto convertToDto(PaymentEntity payment) {
        return new PaymentDto(
                payment.getPaymentId(),
                payment.getBookingId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentMethod());
    }

    @Override
    public List<PaymentDto> getConvertedPayments(List<PaymentEntity> payments) {
        return payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
