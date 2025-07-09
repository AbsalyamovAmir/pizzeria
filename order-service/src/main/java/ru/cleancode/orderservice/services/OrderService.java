package ru.cleancode.orderservice.services;

import ru.cleancode.core.dtos.Order;
import ru.cleancode.core.types.OrderStatus;

import java.util.UUID;

public interface OrderService {
    Order placeOrder(Order order);
    void approveOrder(UUID orderId);
    void rejectOrder(UUID orderId);

    Order getOrderById(UUID orderId);

    Order saveOrder(Order order);

    Order updateOrderStatus(UUID orderId, OrderStatus orderStatus);
}
