package br.unifor.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Getter
    @Setter
    private UUID productId;

    @Getter
    @Setter
    private int quantity;

    @Getter
    @Setter
    private double unitPrice;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}

