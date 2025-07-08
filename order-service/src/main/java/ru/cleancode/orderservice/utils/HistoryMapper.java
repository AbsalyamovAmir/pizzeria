package ru.cleancode.orderservice.utils;

import org.mapstruct.Mapper;
import ru.cleancode.orderservice.dtos.OrderHistory;
import ru.cleancode.orderservice.dtos.OrderHistoryResponse;
import ru.cleancode.orderservice.entities.OrderHistoryEntity;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    OrderHistoryResponse dtoToResponse(OrderHistory order);

    OrderHistory entityToDto(OrderHistoryEntity order);
}
