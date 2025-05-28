package com.unifor.order.service;

import com.unifor.order.DTO.*;
import com.unifor.order.enums.OrderStatus;
import com.unifor.order.model.EnderecoEntrega;
import com.unifor.order.model.Order;
import com.unifor.order.model.OrderItem;
import com.unifor.order.repository.OrderRepository;
import com.unifor.publicador.eventos.NotificarEstoqueEvent;
import com.unifor.publicador.eventos.NovoPedidoEvent;
import com.unifor.publicador.eventos.PagamentoMensagem;
import com.unifor.publicador.service.PedidoEventPublisher;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PedidoEventPublisher pedidoEventPublisher;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${produtos.service.url}")
    private String produtosServiceUrl;

    @RateLimiter(name = "orderService")
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


        Long productId = savedOrder.getOrderItems().get(0).getProductId();
        String url = produtosServiceUrl + "/produtos/" + productId;


        ProdutoResponse produto = restTemplate.getForObject(url, ProdutoResponse.class);
        if (produto == null || produto.getIdFornecedor() == null) {
            throw new RuntimeException("Não foi possível obter o fornecedor para o produto " + productId);
        }

        pedidoEventPublisher.publicarNovoPedido(
            new NovoPedidoEvent(savedOrder.getId(), produto.getIdFornecedor() )
        );

        // 🔁 Publica evento Kafka: pagamento
        PagamentoMensagem pagamentoMensagem = new PagamentoMensagem();
        pagamentoMensagem.setPedidoId(savedOrder.getId());

        DadosPagamento dadosPagamento = new DadosPagamento();
        dadosPagamento.setTipo("cartao"); // ou mapeie de orderDTO.getPayMethod()
        dadosPagamento.setValor(orderDTO.getTotalPrice().doubleValue());

        if (orderDTO.getCartao() != null) {
            dadosPagamento.setNumeroCartao(orderDTO.getCartao().getNumero());
            dadosPagamento.setNomeTitular(orderDTO.getCartao().getNome());
            dadosPagamento.setValidade(orderDTO.getCartao().getValidade());
            dadosPagamento.setCvv(orderDTO.getCartao().getCvv());
        }

        pagamentoMensagem.setDadosPagamento(dadosPagamento);
        pedidoEventPublisher.notificarPagamento(pagamentoMensagem);

        pedidoEventPublisher.publicarAtualizarEstoque(
                new NotificarEstoqueEvent(
                        order.getId(),
                        order.getOrderItems().stream().map(item ->new ProdutoResumidoDTO( item.getProductId(), item.getQuantity())).toList()
                )
        );

        return new OrderDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDTO(order.getId(), order.getUserId(), order.getOrderItems(), order.getTotalPrice()))
                .toList();
    }

    @RateLimiter(name = "orderService")
    public List<OrderDTO> getAllOrderByUserId(Long id) {
        return orderRepository.findByUserId(id).stream()
                .map(OrderDTO::new) // usa o construtor direto
                .toList();
    }
    @RateLimiter(name = "orderService")
    public OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(order -> new OrderDTO(order.getId(), order.getUserId(), order.getOrderItems(), order.getTotalPrice()))
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @RateLimiter(name = "orderService")
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


    @RateLimiter(name = "orderService")
    public OrderDTO deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        orderRepository.delete(order);
        return new OrderDTO(order.getId(), order.getUserId(), order.getOrderItems(), order.getTotalPrice());
    }

    @RateLimiter(name = "orderService")
    @Transactional
    public void atualizarStatus(Long idPedido, String statusRecebido) {

        Order pedido = orderRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + idPedido));

        try {
            OrderStatus novoStatus = OrderStatus.valueOf(statusRecebido.toUpperCase());
            pedido.setStatus(novoStatus);
            orderRepository.save(pedido);
            System.out.println("📦 Pedido " + idPedido + " atualizado para status: " + novoStatus);

            if (OrderStatus.CANCELADO.equals(novoStatus)){
                Hibernate.initialize(pedido.getOrderItems());
                pedidoEventPublisher.publicarAtualizarEstoqueComReposicao(
                        new NotificarEstoqueEvent(
                                pedido.getId(),
                                pedido.getOrderItems().stream().map(item ->new ProdutoResumidoDTO( item.getProductId(), item.getQuantity())).toList()
                        )
                );
            }
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Status inválido recebido: " + statusRecebido);
            // opcional: lançar exception, logar em monitoramento, etc.
        }
    }
}
