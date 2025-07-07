package ru.cleancode.paymentsservice.util;

import org.mapstruct.Mapper;
import ru.cleancode.core.dto.Payment;
import ru.cleancode.paymentsservice.jpa.entity.PaymentEntity;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentEntity dtoToEntity(Payment payment);

    Payment entityToDto(PaymentEntity payment);
}
