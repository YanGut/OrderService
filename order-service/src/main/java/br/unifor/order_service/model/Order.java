package br.unifor.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;

    @Getter
    @Setter
    @Column(nullable = false)
    private UUID userId;

    @Getter
    @Setter
    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDateTime estimatedDelivery;

    @Getter
    @Setter
    @Column(nullable = false)
    private double totalAmount;

    @Getter
    @Setter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
