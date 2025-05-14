package com.unifor.order.service;

import com.unifor.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class GrpcOrderService extends OrderServiceGrpc.OrderServiceGrpc {
    @Autowired
    private OrderService orderService;

    @Override
    public void createOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        Order order = orderService.createOrder(request);
        OrderResponse response = OrderResponse.newBuilder()
                .setId(order.getId())
                .setStatus(order.getStatus())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getOrderById(OrderIdRequest request, StreamObserver<OrderResponse> responseObserver) {
//        Order order = orderService.getOrderById(request.getId());
//        OrderResponse response = OrderResponse.newBuilder()
//                .setId(order.getId())
//                .setStatus(order.getStatus())
//                .build();
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();

        OrderResponse response = OrderResponse.newBuilder()
                .setId(1)
                .setCustomerName("John Doe")
                .setItems("Coffee", "Tea")
                .totalPrice(50.0)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
