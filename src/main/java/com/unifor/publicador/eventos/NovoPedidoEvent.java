package com.unifor.publicador.eventos;

public class NovoPedidoEvent {
    private Long pedidoId;
    private Long clienteId;

    public NovoPedidoEvent(Long pedidoId, Long clienteId) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}
