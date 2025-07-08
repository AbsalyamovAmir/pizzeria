package ru.cleancode.paymentsservice.services.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.cleancode.core.dtos.Payment;
import ru.cleancode.core.dtos.commands.ProcessPaymentCommand;
import ru.cleancode.core.dtos.events.PaymentFailedEvent;
import ru.cleancode.core.dtos.events.PaymentProcessedEvent;
import ru.cleancode.core.exceptions.CreditCardProcessorUnavailableException;
import ru.cleancode.paymentsservice.services.PaymentService;

@Component
@KafkaListener(topics = "${spring.kafka.topic.payments.command-name}")
@RequiredArgsConstructor
@Slf4j
public class PaymentsCommandsHandler {

    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.payments.events-name}")
    private String paymentEventsTopicName;

    @KafkaHandler
    public void handleCommand(@Payload ProcessPaymentCommand command) {
        try {
            Payment payment = Payment.builder()
                    .orderId(command.getOrderId())
                    .productId(command.getProductId())
                    .productPrice(command.getProductPrice())
                    .productQuantity(command.getProductQuantity())
                    .build();
            Payment processedPayment = paymentService.process(payment);
            PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(processedPayment.getOrderId(),
                    processedPayment.getId());
            kafkaTemplate.send(paymentEventsTopicName, paymentProcessedEvent);
        } catch (CreditCardProcessorUnavailableException e) {
            log.error(e.getLocalizedMessage(), e);
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(command.getOrderId(),
                    command.getProductId(),
                    command.getProductQuantity());
            kafkaTemplate.send(paymentEventsTopicName, paymentFailedEvent);
        }
    }
}
