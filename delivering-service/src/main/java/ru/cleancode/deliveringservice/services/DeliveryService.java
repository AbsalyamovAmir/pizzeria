package ru.cleancode.deliveringservice.services;

import ru.cleancode.deliveringservice.dtos.DeliveryStatusResponse;

import java.util.UUID;

public interface DeliveryService {
    DeliveryStatusResponse getDeliveryStatus(UUID deliveryId);
    void processDelivery(UUID orderId);
    void completeDelivery(UUID orderId);
}