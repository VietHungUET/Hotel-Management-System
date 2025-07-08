package com.example.demo.service;

import com.example.demo.entity.PaymentEntity;
import com.example.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentEntity getPaymentByID(int id) {
        return paymentRepository.findById(id).orElse(null);
    }
    public List<PaymentEntity> getAllPayments()  {
        return paymentRepository.findAll();
    }
    public PaymentEntity saveDetailPay(PaymentEntity payment) {
        paymentRepository.save(payment);
        return payment;

    }
    public List<PaymentEntity> getByDate(String payment) {
        return paymentRepository.findByPaymentDate(payment);
    }
    public List<PaymentEntity> getByYear(String year) {
        return paymentRepository.findByPaymentYear(year);
    }
}
