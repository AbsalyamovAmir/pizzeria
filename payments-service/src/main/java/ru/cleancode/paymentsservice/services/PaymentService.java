package ru.cleancode.paymentsservice.services;

import ru.cleancode.core.dtos.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment process(Payment payment);
}
