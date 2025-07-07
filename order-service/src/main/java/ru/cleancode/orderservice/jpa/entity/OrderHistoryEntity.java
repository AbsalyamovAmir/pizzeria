package ru.cleancode.orderservice.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.cleancode.core.types.OrderStatus;

import java.sql.Timestamp;
import java.util.UUID;

@Table(name = "orders_history")
@Entity
@Getter
@Setter
public class OrderHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
