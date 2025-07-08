package ru.cleancode.core.dtos.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveringProccessCommand {
    private UUID orderId;
    private String trackingNumber;
    private String address;
}
