package ru.cleancode.productservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.cleancode.core.dtos.Product;
import ru.cleancode.productservice.dtos.ProductCreationRequest;
import ru.cleancode.productservice.dtos.ProductCreationResponse;
import ru.cleancode.productservice.services.ProductService;
import ru.cleancode.productservice.utils.ProductMapper;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAll() {
        return productService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreationResponse save(@RequestBody @Valid ProductCreationRequest request) {
        Product result = productService.save(productMapper.requestToDto(request));

        return productMapper.dtoToResponse(result);
    }
}
