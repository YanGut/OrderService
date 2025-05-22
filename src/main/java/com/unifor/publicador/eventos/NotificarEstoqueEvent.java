package com.unifor.publicador.eventos;

import com.unifor.order.DTO.ProdutoResumidoDTO;

import javax.management.NotificationFilter;
import java.util.List;

public class NotificarEstoqueEvent {

    Long pedidoId;
    List<ProdutoResumidoDTO> items;


    public NotificarEstoqueEvent() { };

    public NotificarEstoqueEvent(Long pedidoId, List<ProdutoResumidoDTO> items) {
        this.pedidoId = pedidoId;
        this.items = items;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public List<ProdutoResumidoDTO> getItems() {
        return items;
    }

    public void setItems(List<ProdutoResumidoDTO> items) {
        this.items = items;
    }
}
