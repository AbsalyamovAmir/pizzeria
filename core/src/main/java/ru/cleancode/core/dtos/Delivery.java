package ru.cleancode.core.dtos;

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
public class Delivery {
    private UUID id;
    private UUID orderId;
    private String address;
    private DeliveryStatus status;
}
