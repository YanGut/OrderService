package com.unifor.order.service;

import com.unifor.order.DTO.EnderecoEntregaDTO;
import com.unifor.order.DTO.OrderDTO;
import com.unifor.order.enums.OrderStatus;
import com.unifor.order.model.EnderecoEntrega;
import com.unifor.order.model.Order;
import com.unifor.order.model.OrderItem;
import com.unifor.order.repository.OrderRepository;
import com.unifor.publicador.eventos.NovoPedidoEvent;
import com.unifor.publicador.service.PedidoEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PedidoEventPublisher pedidoEventPublisher;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Cria a entidade Order
        Order order = new Order();
        order.setUserId(orderDTO.getCustomerId());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setPayMethod(orderDTO.getPayMethod());
        order.setEnderecoEntrega(new EnderecoEntrega(orderDTO.getEnderecoEntrega()));
        order.setStatus(OrderStatus.CRIADO); // default, mas explícito

        // Cria e associa os itens do pedido
        List<OrderItem> orderItems = orderDTO.getItems().stream()
                .map(itemDTO -> {
                    if (itemDTO.getProductId() == null) {
                        throw new IllegalArgumentException("ProdutoId não pode ser nulo.");
                    }
                    OrderItem item = new OrderItem();
                    item.setProductId(itemDTO.getProductId());
                    item.setQuantity(itemDTO.getQuantity());
                    item.setOrder(order); // relação bidirecional
                    return item;
                })
                .toList();

        order.setOrderItems(orderItems);

        // Persiste no banco
        Order savedOrder = orderRepository.save(order);

        // 🔁 Publica evento Kafka
        pedidoEventPublisher.publicarNovoPedido(
            new NovoPedidoEvent(savedOrder.getId(), savedOrder.getUserId())
        );


        // 💳 Envia dados do cartão para o microserviço de pagamento (se implementado)
    /*
    pagamentoService.enviarParaPagamento(orderDTO.getCartao(), savedOrder);
    */

        return new OrderDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDTO(order.getId(), order.getUserId(), order.getOrderItems(), order.getTotalPrice()))
                .toList();
    }

    public List<OrderDTO> getAllOrderByUserId(Long id) {
        return orderRepository.findByUserId(id).stream()
                .map(OrderDTO::new) // usa o construtor direto
                .toList();
    }

    public OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(order -> new OrderDTO(order.getId(), order.getUserId(), order.getOrderItems(), order.getTotalPrice()))
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderDTO.getId()));

        order.setUserId(orderDTO.getCustomerId());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setPayMethod(orderDTO.getPayMethod());
        order.setStatus(orderDTO.getStatus());

        // Atualiza endereço
        if (orderDTO.getEnderecoEntrega() != null) {
            EnderecoEntregaDTO dto = orderDTO.getEnderecoEntrega();
            EnderecoEntrega endereco = new EnderecoEntrega();
            endereco.setRua(dto.getRua());
            endereco.setNumero(dto.getNumero());
            endereco.setComplemento(dto.getComplemento());
            endereco.setBairro(dto.getBairro());
            endereco.setCidade(dto.getCidade());
            endereco.setEstado(dto.getEstado());
            endereco.setCep(dto.getCep());
            order.setEnderecoEntrega(endereco);
        }

        // Atualiza itens
        if (orderDTO.getItems() != null) {
            List<OrderItem> itens = orderDTO.getItems().stream()
                    .map(dto -> {
                        OrderItem item = new OrderItem();
                        item.setProductId(dto.getProductId());
                        item.setQuantity(dto.getQuantity());
                        item.setOrder(order); // relacionamento bidirecional
                        return item;
                    })
                    .toList();
            order.setOrderItems(itens);
        }

        Order updatedOrder = orderRepository.save(order);
        return new OrderDTO(updatedOrder);
    }


    public OrderDTO deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        orderRepository.delete(order);
        return new OrderDTO(order.getId(), order.getUserId(), order.getOrderItems(), order.getTotalPrice());
    }
}
