package ru.cleancode.orderservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cleancode.core.dtos.Order;
import ru.cleancode.core.dtos.events.OrderApprovedEvent;
import ru.cleancode.core.dtos.events.OrderCreatedEvent;
import ru.cleancode.core.types.OrderStatus;
import ru.cleancode.orderservice.entities.OrderEntity;
import ru.cleancode.orderservice.repositories.OrderRepository;
import ru.cleancode.orderservice.services.OrderService;
import ru.cleancode.orderservice.utils.OrderMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderMapper orderMapper;

    @Value("${spring.kafka.topic.orders.events-name}")
    private String ordersEventsTopicName;

    @Override
    @Transactional
    public Order placeOrder(Order order) {
        order.setStatus(OrderStatus.CREATED);
        OrderEntity entity = orderMapper.dtoToEntity(order);
        orderRepository.save(entity);

        OrderCreatedEvent placedOrder = new OrderCreatedEvent(
                entity.getId(),
                entity.getCustomerId(),
                order.getProductId(),
                order.getProductQuantity(),
                order.getAddress(),
                order.getStatus()
        );
        kafkaTemplate.send(ordersEventsTopicName, placedOrder);

        return orderMapper.entityToDto(entity);
    }

    @Override
    @Transactional
    public void approveOrder(UUID orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No order found with id: " + orderId));
        orderEntity.setStatus(OrderStatus.APPROVED);
        orderRepository.save(orderEntity);
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(orderId);
        kafkaTemplate.send(ordersEventsTopicName, orderApprovedEvent);
    }

    @Override
    @Transactional
    public void rejectOrder(UUID orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No order found with id: " + orderId));
        orderEntity.setStatus(OrderStatus.REJECTED);
        orderRepository.save(orderEntity);
    }

    @Override
    @Transactional
    public Order getOrderById(UUID orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No order found with id: " + orderId));
        return orderMapper.entityToDto(orderEntity);
    }

    @Override
    public Order saveOrder(Order order) {
        OrderEntity entity = orderRepository.save(orderMapper.dtoToEntity(order));
        return orderMapper.entityToDto(entity);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus orderStatus) {
        OrderEntity currentOrderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No order found with id: " + orderId));
        currentOrderEntity.setStatus(orderStatus);
        OrderEntity orderEntity = orderRepository.save(currentOrderEntity);
        return orderMapper.entityToDto(orderEntity);
    }
}
