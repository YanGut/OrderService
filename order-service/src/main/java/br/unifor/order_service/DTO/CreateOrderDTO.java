package br.unifor.order_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CreateOrderDTO {
    @Getter
    private UUID userId;
    @Getter
    private List<UUID> productsId;
    private LocalDateTime orderDate;

    public CreateOrderDTO(UUID user, List<UUID> products) {
        this.userId = user;
        this.productsId = products;
    }

    public CreateOrderDTO() {}

    public void validateData() {
        if (userId == null || productsId.isEmpty() || orderDate == null) {
            throw new IllegalArgumentException("Preencha todos os campos");
        }
    }
}
