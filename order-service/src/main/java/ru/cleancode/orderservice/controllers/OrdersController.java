package ru.cleancode.orderservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.cleancode.core.dtos.Order;
import ru.cleancode.orderservice.dtos.CreateOrderRequest;
import ru.cleancode.orderservice.dtos.CreateOrderResponse;
import ru.cleancode.orderservice.dtos.OrderHistoryResponse;
import ru.cleancode.orderservice.services.OrderHistoryService;
import ru.cleancode.orderservice.services.OrderService;
import ru.cleancode.orderservice.utils.HistoryMapper;
import ru.cleancode.orderservice.utils.OrderMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    private final OrderHistoryService orderHistoryService;
    private final OrderMapper orderMapper;
    private final HistoryMapper historyMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateOrderResponse placeOrder(@RequestBody @Valid CreateOrderRequest request) {
        Order createdOrder = orderService.placeOrder(orderMapper.requestToDto(request));
        return orderMapper.dtoToResponse(createdOrder);
    }

    @GetMapping("/{orderId}/history")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderHistoryResponse> getOrderHistory(@PathVariable UUID orderId) {
        return orderHistoryService.findByOrderId(orderId).stream().map(historyMapper::dtoToResponse).toList();
    }
}
