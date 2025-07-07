package ru.cleancode.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.cleancode.core.dto.Order;
import ru.cleancode.orderservice.dto.CreateOrderRequest;
import ru.cleancode.orderservice.dto.CreateOrderResponse;
import ru.cleancode.orderservice.dto.OrderHistoryResponse;
import ru.cleancode.orderservice.service.OrderHistoryService;
import ru.cleancode.orderservice.service.OrderService;
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
