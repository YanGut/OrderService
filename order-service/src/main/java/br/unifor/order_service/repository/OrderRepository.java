package br.unifor.order_service.repository;

import br.unifor.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findOrderByStringId(String id);
}
