package ru.cleancode.deliveringservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cleancode.core.types.DeliveryStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryResponse {
    private UUID deliveryId;
    private UUID orderId;
    private DeliveryStatus status;
}