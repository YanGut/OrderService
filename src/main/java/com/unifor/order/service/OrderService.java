package com.unifor.order.service;

import com.unifor.order.DTO.OrderDTO;
import com.unifor.order.model.Order;
import com.unifor.order.model.OrderItem;
import com.unifor.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        BigDecimal total = new BigDecimal(0);

        // TODO: Implementar a lógica de cálculo do preço total

        return total;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        List<OrderItem> orderItems = orderDTO.getItems().stream()
                .map(item -> new OrderItem(item.getProductId(), item.getQuantity()))
                .toList();

        Order order = new Order(orderDTO.getCustomerId(), orderItems);
        order.setTotalPrice(calculateTotalPrice(orderItems));
        Order savedOrder = orderRepository.save(order);
        return new OrderDTO(savedOrder.getId(), savedOrder.getUserId(), orderItems, savedOrder.getTotalPrice());
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDTO(order.getId(), order.getUserId(), order.getOrderItems(), order.getTotalPrice()))
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
        order.setOrderItems(orderDTO.getItems());
        order.setTotalPrice(calculateTotalPrice(orderDTO.getItems()));

        Order updatedOrder = orderRepository.save(order);
        return new OrderDTO(updatedOrder.getId(), updatedOrder.getUserId(), updatedOrder.getOrderItems(), updatedOrder.getTotalPrice());
    }

    public OrderDTO deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        orderRepository.delete(order);
        return new OrderDTO(order.getId(), order.getUserId(), order.getOrderItems(), order.getTotalPrice());
    }
}
