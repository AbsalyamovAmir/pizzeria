package ru.cleancode.productservice.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cleancode.core.dtos.Product;
import ru.cleancode.productservice.dtos.ProductCreationRequest;
import ru.cleancode.productservice.dtos.ProductCreationResponse;
import ru.cleancode.productservice.entities.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product entityToDto(ProductEntity product);

    ProductEntity productDtoToEntity(Product product);

    @Mapping(target = "id", ignore = true)
    Product requestToDto(ProductCreationRequest productCreationRequest);

    ProductCreationResponse dtoToResponse(Product product);
}
