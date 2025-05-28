package com.unifor.publicador.eventos;

import com.unifor.order.DTO.DadosPagamento;

public class PagamentoMensagem {
    private Long pedidoId;
    private DadosPagamento dadosPagamento;

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public DadosPagamento getDadosPagamento() {
        return dadosPagamento;
    }

    public void setDadosPagamento(DadosPagamento dadosPagamento) {
        this.dadosPagamento = dadosPagamento;
    }
}

