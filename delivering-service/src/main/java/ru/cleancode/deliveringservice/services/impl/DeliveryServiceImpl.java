package ru.cleancode.deliveringservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cleancode.core.dtos.Delivery;
import ru.cleancode.core.types.DeliveryStatus;
import ru.cleancode.deliveringservice.dtos.DeliveryStatusResponse;
import ru.cleancode.deliveringservice.entities.DeliveryEntity;
import ru.cleancode.deliveringservice.repositories.DeliveryRepository;
import ru.cleancode.deliveringservice.services.DeliveryService;
import ru.cleancode.deliveringservice.utils.DeliveryMapper;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    @Transactional
    public DeliveryStatusResponse getDeliveryStatus(UUID deliveryId) {
        DeliveryEntity entity = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("No delivery found with id: " + deliveryId));
        return deliveryMapper.entityToStatusResponse(entity);
    }

    @Override
    @Transactional
    public void processDelivery(Delivery delivery) {
        DeliveryEntity deliveryEntity = deliveryMapper.dtoToEntity(delivery);
        deliveryEntity.setStatus(DeliveryStatus.PROCESSING);
        deliveryRepository.save(deliveryEntity);
    }

    @Override
    @Transactional
    public void completeDelivery(UUID orderId) {
        DeliveryEntity entity = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No delivery found with order id: " + orderId));;
        entity.setStatus(DeliveryStatus.COMPLETED);
        deliveryRepository.save(entity);
    }

    @Override
    public void failDelivery(UUID orderId) {
        DeliveryEntity entity = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No delivery found with order id: " + orderId));;
        entity.setStatus(DeliveryStatus.FAILED);
        deliveryRepository.save(entity);
    }
}