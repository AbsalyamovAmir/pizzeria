package ru.cleancode.paymentsservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cleancode.core.dtos.Payment;
import ru.cleancode.paymentsservice.entities.PaymentEntity;
import ru.cleancode.paymentsservice.repositories.PaymentRepository;
import ru.cleancode.paymentsservice.services.CreditCardProcessorRemoteService;
import ru.cleancode.paymentsservice.services.PaymentService;
import ru.cleancode.paymentsservice.utils.PaymentMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CreditCardProcessorRemoteService ccpRemoteService;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public Payment process(Payment payment) {
        BigDecimal totalPrice = payment.getProductPrice()
                .multiply(new BigDecimal(payment.getProductQuantity()));
        Random random = new Random();
        ccpRemoteService.process(BigInteger.valueOf(random.nextInt()), totalPrice);
        PaymentEntity paymentEntity = paymentMapper.dtoToEntity(payment);
        paymentRepository.save(paymentEntity);

        Payment processedPayment = paymentMapper.entityToDto(paymentEntity);
        processedPayment.setId(paymentEntity.getId());
        return processedPayment;
    }

    @Override
    @Transactional
    public List<Payment> findAll() {
        return paymentRepository.findAll().stream().map(paymentMapper::entityToDto).collect(Collectors.toList());
    }
}
