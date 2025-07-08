package ru.cleancode.orderservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cleancode.core.types.OrderStatus;
import ru.cleancode.orderservice.dtos.OrderHistory;
import ru.cleancode.orderservice.entities.OrderHistoryEntity;
import ru.cleancode.orderservice.repositories.OrderHistoryRepository;
import ru.cleancode.orderservice.services.OrderHistoryService;
import ru.cleancode.orderservice.utils.HistoryMapper;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final HistoryMapper historyMapper;

    @Override
    public void add(UUID orderId, OrderStatus orderStatus) {
        OrderHistoryEntity entity = new OrderHistoryEntity();
        entity.setOrderId(orderId);
        entity.setStatus(orderStatus);
        entity.setCreatedAt(new Timestamp(new Date().getTime()));
        orderHistoryRepository.save(entity);
    }

    @Override
    public List<OrderHistory> findByOrderId(UUID orderId) {
        List<OrderHistoryEntity> entities = orderHistoryRepository.findByOrderId(orderId);
        return entities.stream().map(historyMapper::entityToDto).toList();
    }
}
