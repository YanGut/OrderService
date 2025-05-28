package com.unifor.order.DTO;

import java.math.BigDecimal;
import java.util.List;

import com.unifor.order.enums.OrderStatus;
import com.unifor.order.model.EnderecoEntrega;
import com.unifor.order.model.Order;
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
    private List<OrderItemDTO> items;
    private BigDecimal totalPrice;
    private CartaoDTO cartao;
    private int payMethod;
    private EnderecoEntregaDTO  enderecoEntrega;
    private OrderStatus status = OrderStatus.CRIADO;



    public OrderDTO(Long id , Long customerId, List<OrderItem> items, BigDecimal totalPrice) {
        this.id = id;
        this.customerId = customerId;
        this.items = items.stream()
                .map(OrderItemDTO::new) // requer construtor em OrderItemDTO
                .toList();
        this.totalPrice = totalPrice;
    }

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.customerId = order.getUserId();
        this.totalPrice = order.getTotalPrice();
        this.payMethod = order.getPayMethod();
        this.status = order.getStatus();

        this.items = order.getOrderItems().stream()
                .map(OrderItemDTO::new)
                .toList();

        if (order.getEnderecoEntrega() != null) {
            EnderecoEntregaDTO enderecoDTO = new EnderecoEntregaDTO();
            enderecoDTO.setRua(order.getEnderecoEntrega().getRua());
            enderecoDTO.setNumero(order.getEnderecoEntrega().getNumero());
            enderecoDTO.setComplemento(order.getEnderecoEntrega().getComplemento());
            enderecoDTO.setBairro(order.getEnderecoEntrega().getBairro());
            enderecoDTO.setCidade(order.getEnderecoEntrega().getCidade());
            enderecoDTO.setEstado(order.getEnderecoEntrega().getEstado());
            enderecoDTO.setCep(order.getEnderecoEntrega().getCep());
            this.enderecoEntrega = enderecoDTO;
        }
    }

    public OrderDTO() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return this.customerId; }

    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public List<OrderItemDTO> getItems() { return items; }

    public void setItems(List<OrderItemDTO> items) { this.items = items; }

    public BigDecimal getTotalPrice() { return totalPrice; }

    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public CartaoDTO getCartao() {
        return cartao;
    }

    public void setCartao(CartaoDTO cartao) {
        this.cartao = cartao;
    }

    public EnderecoEntregaDTO getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(EnderecoEntregaDTO enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
