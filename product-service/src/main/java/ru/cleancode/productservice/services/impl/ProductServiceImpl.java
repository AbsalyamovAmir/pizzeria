package ru.cleancode.productservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cleancode.core.dtos.Product;
import ru.cleancode.core.exceptions.ProductInsufficientQuantityException;
import ru.cleancode.productservice.entities.ProductEntity;
import ru.cleancode.productservice.repositories.ProductRepository;
import ru.cleancode.productservice.services.ProductService;
import ru.cleancode.productservice.utils.ProductMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public Product reserve(Product desiredProduct, UUID orderId) {
        ProductEntity productEntity = productRepository.findById(desiredProduct.getId())
                .orElseThrow(() -> new IllegalArgumentException("No product found with id: " + desiredProduct.getId()));
        if (desiredProduct.getQuantity() > productEntity.getQuantity()) {
            throw new ProductInsufficientQuantityException(productEntity.getId(), orderId);
        }

        productEntity.setQuantity(productEntity.getQuantity() - desiredProduct.getQuantity());
        productRepository.save(productEntity);

        Product reservedProduct = productMapper.entityToDto(productEntity);
        reservedProduct.setQuantity(desiredProduct.getQuantity());
        return reservedProduct;
    }

    @Override
    @Transactional
    public void cancelReservation(Product productToCancel, UUID orderId) {
        ProductEntity productEntity = productRepository.findById(productToCancel.getId())
                .orElseThrow(() -> new IllegalArgumentException("No product found with id: " + productToCancel.getId()));
        productEntity.setQuantity(productEntity.getQuantity() + productToCancel.getQuantity());
        productRepository.save(productEntity);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity productEntity = productRepository.save(productMapper.productDtoToEntity(product));
        return productMapper.entityToDto(productEntity);
    }

    @Override
    @Transactional
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
