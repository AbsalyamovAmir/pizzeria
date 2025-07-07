package ru.cleancode.paymentsservice.service;

import ru.cleancode.core.dto.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment process(Payment payment);
}
