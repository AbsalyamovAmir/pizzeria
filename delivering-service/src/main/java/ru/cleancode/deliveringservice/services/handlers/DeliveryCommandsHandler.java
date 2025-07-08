package ru.cleancode.deliveringservice.services.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.cleancode.core.dtos.commands.DeliveringProccessCommand;
import ru.cleancode.core.dtos.commands.StartDeliveryCommand;
import ru.cleancode.core.dtos.events.ShipmentDeliveredEvent;
import ru.cleancode.core.dtos.events.ShipmentDeliveringFailedEvent;
import ru.cleancode.core.dtos.events.ShipmentDispatchedEvent;
import ru.cleancode.core.exceptions.CourierGotLostExpcetion;
import ru.cleancode.deliveringservice.services.DeliveryService;

import java.util.UUID;

@Component
@KafkaListener(topics = "${spring.kafka.topic.deliveries.command-name}")
@RequiredArgsConstructor
@Slf4j
public class DeliveryCommandsHandler {
    private final DeliveryService deliveryService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.deliveries.events-name}")
    private String deliveriesEventsTopicName;

    @KafkaHandler
    public void handleEvent(@Payload StartDeliveryCommand startDeliveryCommand) {
        deliveryService.processDelivery(startDeliveryCommand.getOrderId());

        ShipmentDispatchedEvent dispatchedEvent = new ShipmentDispatchedEvent(
                startDeliveryCommand.getOrderId(),
                "TRACK-" + UUID.randomUUID().toString().substring(0, 8),
                startDeliveryCommand.getAddress()
        );
        kafkaTemplate.send(deliveriesEventsTopicName, dispatchedEvent);
    }

    @KafkaHandler
    public void handleEvent(@Payload DeliveringProccessCommand deliveringProccessCommand) {
        try {
            deliveryService.completeDelivery(deliveringProccessCommand.getOrderId());
            ShipmentDeliveredEvent deliveredEvent = new ShipmentDeliveredEvent(
                    deliveringProccessCommand.getOrderId(),
                    deliveringProccessCommand.getTrackingNumber(),
                    deliveringProccessCommand.getAddress());

            kafkaTemplate.send(deliveriesEventsTopicName, deliveredEvent);
        } catch (CourierGotLostExpcetion e) {
            log.error(e.getLocalizedMessage(), e);
            ShipmentDeliveringFailedEvent shipmentFailedEvent = new ShipmentDeliveringFailedEvent(deliveringProccessCommand.getOrderId(),
                    deliveringProccessCommand.getTrackingNumber(),
                    deliveringProccessCommand.getAddress());
            kafkaTemplate.send(deliveriesEventsTopicName, shipmentFailedEvent);
        }
    }
}
