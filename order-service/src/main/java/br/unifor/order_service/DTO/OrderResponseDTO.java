package br.unifor.order_service.DTO;

import br.unifor.order_service.model.Order;

public class OrderResponseDTO {
    private Order order;

    public OrderResponseDTO(Order order) { this.order = order; }

    public OrderResponseDTO() {}

    public Order getOrder() { return order; }

    public void setOrder(Order order) { this.order = order; }
}
