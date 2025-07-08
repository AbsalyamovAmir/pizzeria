package ru.cleancode.orderservice.services;

import ru.cleancode.core.types.OrderStatus;
import ru.cleancode.orderservice.dtos.OrderHistory;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryService {
    void add(UUID orderId, OrderStatus orderStatus);

    List<OrderHistory> findByOrderId(UUID orderId);
}
