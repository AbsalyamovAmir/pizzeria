package ru.cleancode.deliveringservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.cleancode.core.types.DeliveryStatus;

import java.util.UUID;

@Table(name = "deliveries")
@Entity
@Getter
@Setter
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeliveryStatus status;
}