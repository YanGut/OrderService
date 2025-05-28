package com.unifor.consumidor.service;

import com.unifor.order.enums.OrderStatus;
import com.unifor.order.model.Order;
import com.unifor.order.repository.OrderRepository;
import com.unifor.consumidor.event.PagamentoMensagem;
import com.unifor.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagamentoStatusConsumer {

    @Autowired
    private OrderService orderService;


    @KafkaListener(topics = "pagamentos-processados", groupId = "order-service")
    public void consumirStatusPagamento(PagamentoMensagem mensagem) {
        Long idPedido = mensagem.getPedidoId();
        String statusRecebido = mensagem.getStatus();
        orderService.atualizarStatus(idPedido, statusRecebido);
    }

    @KafkaListener(topics = "pedido-status-atualizado", groupId = "order-service")
    public void consumirStatusEnviado(PagamentoMensagem mensagem) {
        Long idPedido = mensagem.getPedidoId();
        String statusRecebido = mensagem.getStatus();
        orderService.atualizarStatus(idPedido, statusRecebido);
    }
}
