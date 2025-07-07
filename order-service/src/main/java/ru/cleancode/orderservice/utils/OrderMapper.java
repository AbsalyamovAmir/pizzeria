package ru.cleancode.orderservice.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cleancode.core.dto.Order;
import ru.cleancode.orderservice.dto.CreateOrderRequest;
import ru.cleancode.orderservice.dto.CreateOrderResponse;
import ru.cleancode.orderservice.jpa.entity.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "status", ignore = true)
    Order requestToDto(CreateOrderRequest request);

    @Mapping(target = "id", source = "orderId")
    @Mapping(target = "status", ignore = true)
    OrderEntity dtoToEntity(Order order);

    @Mapping(target = "orderId", source = "id")
    Order entityToDto(OrderEntity entity);

    CreateOrderResponse dtoToResponse(Order order);
}
