package com.unifor.order.controller;

import com.unifor.order.DTO.OrderDTO;
import com.unifor.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Operações para gerenciamento de pedidos")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "Criar novo pedido", responses = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados do pedido")
    })
    public ResponseEntity<OrderDTO> criarPedido(@RequestBody OrderDTO orderDTO) {
        OrderDTO novoPedido = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Listar todos os pedidos")
    public ResponseEntity<List<OrderDTO>> listarPedidos(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getAllOrderByUserId(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping
    @Operation(summary = "Atualizar pedido existente", responses = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderDTO> atualizarPedido(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.updateOrder(orderDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pedido por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Pedido excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderDTO> excluirPedido(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deleteOrderById(id));
    }
}
