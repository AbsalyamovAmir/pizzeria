package ru.cleancode.orderservice.services.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.cleancode.core.dtos.commands.ApproveOrderCommand;
import ru.cleancode.core.dtos.commands.RejectOrderCommand;
import ru.cleancode.orderservice.services.OrderService;

@Component
@KafkaListener(topics="${spring.kafka.topic.orders.command-name}")
@RequiredArgsConstructor
public class OrderCommandsHandler {

    private final OrderService orderService;

    @KafkaHandler
    public void handleCommand(@Payload ApproveOrderCommand approveOrderCommand) {
        orderService.approveOrder(approveOrderCommand.getOrderId());
    }

    @KafkaHandler
    public void handleCommand(@Payload RejectOrderCommand rejectOrderCommand) {
        orderService.rejectOrder(rejectOrderCommand.getOrderId());
    }
}
