package ru.cleancode.productservice.services.handler;

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
import ru.cleancode.core.dtos.Product;
import ru.cleancode.core.dtos.commands.CancelProductReservationCommand;
import ru.cleancode.core.dtos.commands.ProductReservationCancelledEvent;
import ru.cleancode.core.dtos.commands.ReserveProductCommand;
import ru.cleancode.core.dtos.events.ProductReservationFailedEvent;
import ru.cleancode.core.dtos.events.ProductReservedEvent;
import ru.cleancode.productservice.services.ProductService;

@Component
@KafkaListener(topics = "${spring.kafka.topic.products.command-name}")
@RequiredArgsConstructor
@Slf4j
public class ProductCommandsHandler {

    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.products.events-name}")
    private String productEventsTopicName;


    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand command) {

        try {
            Product desiredProduct = Product.builder()
                    .id(command.getProductId())
                    .quantity(command.getProductQuantity())
                    .build();
            Product reservedProduct = productService.reserve(desiredProduct, command.getOrderId());
            ProductReservedEvent productReservedEvent = new ProductReservedEvent(command.getOrderId(),
                    command.getProductId(),
                    reservedProduct.getPrice(),
                    command.getProductQuantity());
            kafkaTemplate.send(productEventsTopicName, productReservedEvent);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            ProductReservationFailedEvent productReservationFailedEvent =
                    new ProductReservationFailedEvent(command.getProductId(),
                            command.getOrderId(),
                            command.getProductQuantity());
            kafkaTemplate.send(productEventsTopicName, productReservationFailedEvent);
        }
    }

    @KafkaHandler
    public void handleCommand(@Payload CancelProductReservationCommand command) {
        Product productToCancel = Product.builder()
                .id(command.getProductId())
                .quantity(command.getProductQuantity())
                .build();
        productService.cancelReservation(productToCancel, command.getOrderId());

        ProductReservationCancelledEvent productReservationCancelledEvent =
                new ProductReservationCancelledEvent(command.getProductId(), command.getOrderId());
        kafkaTemplate.send(productEventsTopicName, productReservationCancelledEvent);
    }
}
