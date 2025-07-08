package ru.cleancode.deliveringservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.cleancode.deliveringservice.dtos.DeliveryStatusResponse;
import ru.cleancode.deliveringservice.services.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @GetMapping("/{deliveryId}/status")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryStatusResponse getDeliveryStatus(@PathVariable UUID deliveryId) {
        return deliveryService.getDeliveryStatus(deliveryId);
    }
}