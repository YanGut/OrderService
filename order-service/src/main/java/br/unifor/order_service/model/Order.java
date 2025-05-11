package br.unifor.order_service.model;

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
    private String orderId;

    @Getter
    @Setter
    @Column(nullable = false)
    private String userId;

    @Getter
    @Setter
    @Column(nullable = false)
    private String shippingAddress;

    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Getter
    @Setter
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

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
