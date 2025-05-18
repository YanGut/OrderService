package com.unifor.publicador.service;

import com.unifor.publicador.eventos.NovoPedidoEvent;
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

}
