package ru.cleancode.paymentsservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import ru.cleancode.core.dtos.CreditCardProcessRequest;
import ru.cleancode.core.exceptions.CreditCardProcessorUnavailableException;
import ru.cleancode.paymentsservice.clients.AmirBankServiceClient;
import ru.cleancode.paymentsservice.services.CreditCardProcessorRemoteService;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class CreditCardProcessorRemoteServiceImpl implements CreditCardProcessorRemoteService {
    private final AmirBankServiceClient bankServiceClient;

    @Override
    public void process(BigInteger cardNumber, BigDecimal paymentAmount) {
        try {
            CreditCardProcessRequest request = new CreditCardProcessRequest(cardNumber, paymentAmount);
            bankServiceClient.processCreditCard(request);
        } catch (ResourceAccessException e) {
            throw new CreditCardProcessorUnavailableException(e);
        }
    }
}
