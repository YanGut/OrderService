package br.unifor.order.service;

import br.unifor.order.DTO.CreateOrderDTO;
import br.unifor.order.DTO.OrderResponseDTO;
import br.unifor.order.model.Order;
import br.unifor.order.model.OrderItem;
import br.unifor.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(CreateOrderDTO createOrderDTO) {
        createOrderDTO.validateData();

        List<UUID> productsId = createOrderDTO.getProductsId();
        List<OrderItem> orderItems = new ArrayList<>();

        for (UUID productId : productsId) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId);
            orderItems.add(orderItem);
        }

        Order order = new Order();
        order.setOrderItems(orderItems);
        order.setUserId(createOrderDTO.getUserId());

        return orderRepository.save(order);
    }

    public Order getOrderById(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            throw new RuntimeException("Pedido não encontrado");
        }

        Order order = orderOptional.get();

        return order;
    }

    public List<Order> getOrders() {
        List<Order> orderList = orderRepository.findAll();

        if (orderList.isEmpty()) {
            throw new RuntimeException("Nenhum pedido encontrado");
        }

        return orderList;
    }
}
