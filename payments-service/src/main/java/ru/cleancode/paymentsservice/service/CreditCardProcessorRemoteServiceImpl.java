package ru.cleancode.paymentsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import ru.cleancode.core.dto.CreditCardProcessRequest;
import ru.cleancode.core.exceptions.CreditCardProcessorUnavailableException;
import ru.cleancode.paymentsservice.client.AmirBankServiceClient;

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
