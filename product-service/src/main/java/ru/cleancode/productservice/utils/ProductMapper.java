package ru.cleancode.productservice.utils;

import org.mapstruct.Mapper;
import ru.cleancode.core.dtos.Product;
import ru.cleancode.productservice.entities.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product entityToDto(ProductEntity product);

    ProductEntity productDtoToEntity(Product product);
}
