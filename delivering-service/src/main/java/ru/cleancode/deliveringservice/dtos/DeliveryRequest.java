package ru.cleancode.deliveringservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryRequest {
    private UUID orderId;
    private String address;
    private String recipientName;
    private String recipientPhone;
}