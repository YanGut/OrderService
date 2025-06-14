package com.unifor.order.DTO;

import com.unifor.order.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private int quantity;

    public OrderItemDTO(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    
    public OrderItemDTO() { }

    public OrderItemDTO(OrderItem item) {
        this.productId = item.getProductId();
        this.quantity = item.getQuantity();
    }

    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
