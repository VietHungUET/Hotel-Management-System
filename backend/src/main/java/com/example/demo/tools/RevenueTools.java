package com.example.demo.tools;

import com.example.demo.service.PaymentService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RevenueTools {

    private final PaymentService paymentService;

    @Tool("Get monthly revenue for a specific year. Returns revenue for each month (12 values).")
    public String getRevenueByYear(String year) {
        List<Double> revenue = paymentService.getRevenueByYear(year);

        StringBuilder result = new StringBuilder();
        result.append("Revenue for year ").append(year).append(":\n");

        String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

        double total = 0;
        for (int i = 0; i < revenue.size() && i < 12; i++) {
            double monthRevenue = revenue.get(i);
            total += monthRevenue;
            result.append(String.format("%s: %.0f VND\n", months[i], monthRevenue));
        }

        result.append(String.format("\nTotal: %.0f VND", total));
        result.append(String.format("\nAverage: %.0f VND/month", total / 12));

        return result.toString();
    }

    @Tool("Get all payment records with details")
    public String getAllPayments() {
        var payments = paymentService.getAllPayments();

        if (payments.isEmpty()) {
            return "No payment records found";
        }

        return paymentService.getConvertedPayments(payments).stream()
                .map(payment -> String.format("Payment #%d - Booking: %d, Amount: %.0f VND, Date: %s, Method: %s",
                        payment.getPaymentId(),
                        payment.getBookingId(),
                        payment.getAmount(),
                        payment.getPaymentDate(),
                        payment.getPaymentMethod()))
                .collect(Collectors.joining("\n"));
    }

    @Tool("Calculate total revenue summary including count and average")
    public String getRevenueSummary() {
        var payments = paymentService.getAllPayments();

        if (payments.isEmpty()) {
            return "No payments recorded";
        }

        double total = payments.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        long count = payments.size();
        double average = total / count;

        return String.format("""
                Revenue Summary:
                Total Payments: %d
                Total Revenue: %.0f VND
                Average Payment: %.0f VND
                """, count, total, average);
    }
}