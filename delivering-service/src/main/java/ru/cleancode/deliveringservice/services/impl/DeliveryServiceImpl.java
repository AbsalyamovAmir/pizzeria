package ru.cleancode.deliveringservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cleancode.core.types.DeliveryStatus;
import ru.cleancode.deliveringservice.dtos.DeliveryStatusResponse;
import ru.cleancode.deliveringservice.entities.DeliveryEntity;
import ru.cleancode.deliveringservice.repositories.DeliveryRepository;
import ru.cleancode.deliveringservice.services.DeliveryService;
import ru.cleancode.deliveringservice.utils.DeliveryMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public DeliveryStatusResponse getDeliveryStatus(UUID deliveryId) {
        DeliveryEntity entity = deliveryRepository.findById(deliveryId).orElseThrow();
        return deliveryMapper.entityToStatusResponse(entity);
    }

    @Override
    public void processDelivery(UUID orderId) {
        DeliveryEntity entity = deliveryRepository.findByOrderId(orderId);
        entity.setStatus(DeliveryStatus.PROCESSING);
        entity.setTrackingNumber(UUID.randomUUID().toString());
        deliveryRepository.save(entity);
    }

    @Override
    public void completeDelivery(UUID orderId) {
        DeliveryEntity entity = deliveryRepository.findByOrderId(orderId);
        entity.setStatus(DeliveryStatus.COMPLETED);
        deliveryRepository.save(entity);
    }
}