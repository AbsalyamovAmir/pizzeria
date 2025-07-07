package ru.cleancode.productservice.util;

import org.mapstruct.Mapper;
import ru.cleancode.core.dto.Product;
import ru.cleancode.productservice.jpa.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product entityToDto(ProductEntity product);

    ProductEntity productDtoToEntity(Product product);
}
