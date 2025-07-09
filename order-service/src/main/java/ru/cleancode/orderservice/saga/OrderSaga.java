package ru.cleancode.orderservice.saga;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.cleancode.core.dtos.Order;
import ru.cleancode.core.dtos.commands.*;
import ru.cleancode.core.dtos.events.*;
import ru.cleancode.core.types.OrderStatus;
import ru.cleancode.orderservice.services.OrderHistoryService;
import ru.cleancode.orderservice.services.OrderService;

import java.util.UUID;

@Component
@KafkaListener(topics = {
        "${spring.kafka.topic.orders.events-name}",
        "${spring.kafka.topic.products.events-name}",
        "${spring.kafka.topic.payments.events-name}",
        "${spring.kafka.topic.deliveries.events-name}"
})
@RequiredArgsConstructor
public class OrderSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final OrderHistoryService orderHistoryService;

    private final OrderService orderService;

    @Value("${spring.kafka.topic.payments.command-name}")
    private String paymentsCommandsTopicName;

    @Value("${spring.kafka.topic.orders.command-name}")
    private String ordersCommandsTopicName;

    @Value("${spring.kafka.topic.products.command-name}")
    private String productsCommandsTopicName;

    @Value("${spring.kafka.topic.deliveries.command-name}")
    private String deliveriesCommandsTopicName;

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent event) {

        ReserveProductCommand command = new ReserveProductCommand(
                event.getProductId(),
                event.getProductQuantity(),
                event.getOrderId()
        );

        kafkaTemplate.send(productsCommandsTopicName, command);
        orderHistoryService.add(event.getOrderId(), event.getOrderStatus());
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservedEvent event) {

        ProcessPaymentCommand processPaymentCommand = new ProcessPaymentCommand(
                event.getOrderId(),
                event.getProductId(),
                event.getProductPrice(),
                event.getProductQuantity());
        kafkaTemplate.send(paymentsCommandsTopicName, processPaymentCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentProcessedEvent event) {

        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(event.getOrderId());
        kafkaTemplate.send(ordersCommandsTopicName, approveOrderCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload OrderApprovedEvent event) {
        Order currentOrder = orderService.getOrderById(event.getOrderId());
        StartDeliveryCommand startDeliveryCommand = new StartDeliveryCommand(currentOrder.getOrderId(), currentOrder.getAddress());
        kafkaTemplate.send(deliveriesCommandsTopicName, startDeliveryCommand);

        Order order = orderService.updateOrderStatus(event.getOrderId(), OrderStatus.APPROVED);
        orderHistoryService.add(order.getOrderId(), order.getStatus());
    }

    @KafkaHandler
    public void handleEvent(@Payload ShipmentDispatchedEvent event) {
        Order order = orderService.updateOrderStatus(event.getOrderId(), OrderStatus.DELIVERING);
        orderHistoryService.add(order.getOrderId(), order.getStatus());

        DeliveringProccessCommand deliveringProccessCommand = new DeliveringProccessCommand(
                event.getOrderId(),
                event.getTrackingNumber(),
                event.getAddress()
        );
        kafkaTemplate.send(deliveriesCommandsTopicName, deliveringProccessCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload ShipmentDeliveredEvent event) {
        Order order = orderService.updateOrderStatus(event.getOrderId(), OrderStatus.DONE);
        orderHistoryService.add(order.getOrderId(), order.getStatus());
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentFailedEvent event) {
        CancelProductReservationCommand cancelProductReservationCommand =
                new CancelProductReservationCommand(
                        event.getProductId(),
                        event.getOrderId(),
                        event.getProductQuantity());
        kafkaTemplate.send(productsCommandsTopicName, cancelProductReservationCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservationFailedEvent event) {
        rejectOrder(event.getOrderId());
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservationCancelledEvent event) {
        rejectOrder(event.getOrderId());
    }

    private void rejectOrder(UUID event) {
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(event);
        kafkaTemplate.send(ordersCommandsTopicName, rejectOrderCommand);
        orderHistoryService.add(event, OrderStatus.REJECTED);
    }
}
