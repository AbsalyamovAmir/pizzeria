package ru.cleancode.deliveringservice.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cleancode.core.dtos.Delivery;
import ru.cleancode.deliveringservice.dtos.DeliveryStatusResponse;
import ru.cleancode.deliveringservice.entities.DeliveryEntity;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    //TODO костыль
    @Mapping(target = "id", ignore = true)
    DeliveryEntity dtoToEntity(Delivery delivery);

    DeliveryStatusResponse entityToStatusResponse(DeliveryEntity entity);
}
