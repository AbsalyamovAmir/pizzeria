package ru.cleancode.paymentsservice.utils;

import org.mapstruct.Mapper;
import ru.cleancode.core.dtos.Payment;
import ru.cleancode.paymentsservice.entities.PaymentEntity;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentEntity dtoToEntity(Payment payment);

    Payment entityToDto(PaymentEntity payment);
}
