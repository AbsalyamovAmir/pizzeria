package ru.cleancode.orderservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.cleancode.core.types.OrderStatus;

import java.util.UUID;

@Table(name = "orders")
@Entity
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_quantity")
    private Integer productQuantity;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
}
