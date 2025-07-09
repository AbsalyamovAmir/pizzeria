package ru.cleancode.deliveringservice.services;

import ru.cleancode.core.dtos.Delivery;
import ru.cleancode.deliveringservice.dtos.DeliveryStatusResponse;

import java.util.UUID;

public interface DeliveryService {
    DeliveryStatusResponse getDeliveryStatus(UUID deliveryId);
    void processDelivery(Delivery delivery);
    void completeDelivery(UUID orderId);
    void failDelivery(UUID orderId);
}