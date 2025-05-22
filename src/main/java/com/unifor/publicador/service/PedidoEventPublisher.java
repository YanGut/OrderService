package com.unifor.publicador.service;

import com.unifor.publicador.eventos.NotificarEstoqueEvent;
import com.unifor.publicador.eventos.NovoPedidoEvent;
import com.unifor.publicador.eventos.PagamentoMensagem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoEventPublisher {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publicarNovoPedido(NovoPedidoEvent evento) {
        kafkaTemplate.send("pedido-criado", evento);
        System.out.println("📤 Evento de novo pedido enviado para Kafka: " + evento);
    }

    public void publicarAtualizarEstoque(NotificarEstoqueEvent evento) {
        kafkaTemplate.send("atualizar-estoque", evento);
        System.out.println("📤 Evento de novo pedido enviado para Kafka: " + evento);
    }

    public void publicarAtualizarEstoqueComReposicao(NotificarEstoqueEvent evento) {
        kafkaTemplate.send("repor-estoque", evento);
        System.out.println("📤 Evento de novo pedido enviado para Kafka: " + evento);
    }


    public void notificarPagamento(PagamentoMensagem mensagem) {
        kafkaTemplate.send("pagamentos-pendentes", mensagem);
        System.out.println("📤 Evento de novo pedido enviado para Kafka: " + mensagem);
    }
}
