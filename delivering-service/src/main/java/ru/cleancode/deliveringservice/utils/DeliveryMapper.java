package ru.cleancode.deliveringservice.utils;

import org.mapstruct.Mapper;
import ru.cleancode.deliveringservice.dtos.DeliveryResponse;
import ru.cleancode.deliveringservice.dtos.DeliveryStatusResponse;
import ru.cleancode.deliveringservice.entities.DeliveryEntity;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryResponse entityToResponse(DeliveryEntity entity);
    DeliveryStatusResponse entityToStatusResponse(DeliveryEntity entity);
}
