package ru.cleancode.paymentsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cleancode.core.dto.Payment;
import ru.cleancode.paymentsservice.jpa.entity.PaymentEntity;
import ru.cleancode.paymentsservice.jpa.repository.PaymentRepository;
import ru.cleancode.paymentsservice.util.PaymentMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public static final String SAMPLE_CREDIT_CARD_NUMBER = "374245455400126";
    private final PaymentRepository paymentRepository;
    private final CreditCardProcessorRemoteService ccpRemoteService;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment process(Payment payment) {
        BigDecimal totalPrice = payment.getProductPrice()
                .multiply(new BigDecimal(payment.getProductQuantity()));
        ccpRemoteService.process(new BigInteger(SAMPLE_CREDIT_CARD_NUMBER), totalPrice);
        PaymentEntity paymentEntity = paymentMapper.dtoToEntity(payment);
        paymentRepository.save(paymentEntity);

        Payment processedPayment = paymentMapper.entityToDto(paymentEntity);
        processedPayment.setId(paymentEntity.getId());
        return processedPayment;
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll().stream().map(paymentMapper::entityToDto).collect(Collectors.toList());
    }
}
