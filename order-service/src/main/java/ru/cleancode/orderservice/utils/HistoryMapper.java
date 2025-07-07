package ru.cleancode.orderservice.utils;

import org.mapstruct.Mapper;
import ru.cleancode.orderservice.dto.OrderHistory;
import ru.cleancode.orderservice.dto.OrderHistoryResponse;
import ru.cleancode.orderservice.jpa.entity.OrderHistoryEntity;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    OrderHistoryResponse dtoToResponse(OrderHistory order);

    OrderHistory entityToDto(OrderHistoryEntity order);
}
