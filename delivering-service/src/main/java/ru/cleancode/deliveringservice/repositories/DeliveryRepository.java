package ru.cleancode.deliveringservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cleancode.deliveringservice.entities.DeliveryEntity;

import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, UUID> {
    DeliveryEntity findByOrderId(UUID orderId);
}