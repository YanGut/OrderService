package com.unifor.order.DTO;

import java.math.BigDecimal;
import java.util.List;

import com.unifor.order.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private Long customerId;
    private List<OrderItem> items;
    private BigDecimal totalPrice;

    public OrderDTO(Long customerId, List<OrderItem> items, BigDecimal totalPrice) {
        this.customerId = customerId;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public OrderDTO() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return this.customerId; }

    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public List<OrderItem> getItems() { return items; }

    public void setItems(List<OrderItem> items) { this.items = items; }

    public BigDecimal getTotalPrice() { return totalPrice; }

    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}
