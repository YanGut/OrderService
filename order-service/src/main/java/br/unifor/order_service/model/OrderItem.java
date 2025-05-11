package br.unifor.order_service.model;

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
    private String id;

    @Getter
    @Setter
    private String productId;

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

