package ru.cleancode.core.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentDeliveredEvent {
    private UUID orderId;
    private String trackingNumber;
    private String address;
}
