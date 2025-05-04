package br.unifor.order.DTO;

import br.unifor.order.model.Order;

public class OrderResponseDTO {
    private Order order;

    public OrderResponseDTO(Order order) { this.order = order; }

    public OrderResponseDTO() {}

    public Order getOrder() { return order; }

    public void setOrder(Order order) { this.order = order; }
}
