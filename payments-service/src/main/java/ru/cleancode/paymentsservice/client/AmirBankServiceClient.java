package ru.cleancode.paymentsservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.cleancode.core.dto.CreditCardProcessRequest;

@FeignClient(name = "bank-service", url = "http://localhost:8083/bank-service")
public interface AmirBankServiceClient {
    @PostMapping("/api/process")
    @CircuitBreaker(name = "bankService")
    @Retry(name = "bankService")
    void processCreditCard(CreditCardProcessRequest request);
}
