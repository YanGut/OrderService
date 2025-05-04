//package br.unifor.order.service;
//
//import br.unifor.order.model.Order;
//import br.unifor.order.model.OrderItem;
//
//import br.unifor.order.repository.OrderRepository;
//
//import io.grpc.stub.StreamObserver;
//import net.devh.boot.grpc.server.service.GrpcService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@GrpcService
//public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private ProductServiceClient productServiceClient;
//
//    @Autowired
//    private PaymentServiceClient paymentServiceClient;
//
//    @Override
//    @Transactional
//    public void createOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
//        // 1. Validate product availability with Product Service
//        boolean productsAvailable = productServiceClient.checkProductsAvailability(
//                request.getItemsList().stream()
//                        .map(item -> item.getProductId())
//                        .collect(Collectors.toList())
//        );
//
//        if (!productsAvailable) {
//            // Handle unavailable products
//            responseObserver.onError(new RuntimeException("Some products are not available"));
//            return;
//        }
//
//        // 2. Process payment through Payment Service
//        boolean paymentProcessed = paymentServiceClient.processPayment(
//                request.getUserId(),
//                request.getPaymentInfo().getPaymentMethodId(),
//                request.getPaymentInfo().getTotalAmount()
//        );
//
//        if (!paymentProcessed) {
//            // Handle payment failure
//            responseObserver.onError(new RuntimeException("Payment processing failed"));
//            return;
//        }
//
//        // 3. Create the order in the database
//        Order order = new Order();
//        order.setOrderId(UUID.randomUUID());
//        order.setUserId(request.getUserId());
//        order.setStatus("CREATED");
//        order.setShippingAddress(request.getShippingAddress());
//        order.setCreatedAt(LocalDateTime.now());
//        order.setTotalAmount(request.getPaymentInfo().getTotalAmount());
//
//        // 4. Add order items
//        List<OrderItem> orderItems = request.getItemsList().stream()
//                .map(item -> {
//                    OrderItem orderItem = new OrderItem();
//                    orderItem.setProductId(item.getProductId());
//                    orderItem.setQuantity(item.getQuantity());
//                    orderItem.setUnitPrice(item.getUnitPrice());
//                    orderItem.setOrder(order);
//                    return orderItem;
//                })
//                .collect(Collectors.toList());
//
//        order.setOrderItems(orderItems);
//
//        orderRepository.save(order);
//
//        // 5. Update inventory through Product Service
//        productServiceClient.updateInventory(
//                request.getItemsList().stream()
//                        .map(item -> new ProductServiceClient.InventoryUpdate(
//                                item.getProductId(), item.getQuantity()))
//                        .collect(Collectors.toList())
//        );
//
//        // 6. Build and send response
//        OrderResponse response = buildOrderResponse(order);
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void getOrderStatus(OrderStatusRequest request, StreamObserver<OrderResponse> responseObserver) {
//        orderRepository.findById(request.getOrderId())
//                .ifPresentOrElse(
//                        order -> {
//                            OrderResponse response = buildOrderResponse(order);
//                            responseObserver.onNext(response);
//                            responseObserver.onCompleted();
//                        },
//                        () -> responseObserver.onError(new RuntimeException("Order not found"))
//                );
//    }
//
//    @Override
//    public void getOrdersForUser(OrdersForUserRequest request, StreamObserver<OrdersList> responseObserver) {
//        int page = Math.max(0, request.getPage());
//        int size = Math.max(1, request.getSize());
//
//        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(
//                request.getUserId(), page, size);
//
//        int totalCount = orderRepository.countByUserId(request.getUserId());
//
//        OrdersList.Builder ordersListBuilder = OrdersList.newBuilder();
//        ordersListBuilder.setTotalCount(totalCount);
//
//        orders.forEach(order ->
//                ordersListBuilder.addOrders(buildOrderResponse(order))
//        );
//
//        responseObserver.onNext(ordersListBuilder.build());
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    @Transactional
//    public void cancelOrder(OrderStatusRequest request, StreamObserver<OrderResponse> responseObserver) {
//        orderRepository.findById(request.getOrderId())
//                .ifPresentOrElse(
//                        order -> {
//                            // Only allow cancellation if order is not completed or already cancelled
//                            if (order.getStatus().equals("COMPLETED") || order.getStatus().equals("CANCELLED")) {
//                                responseObserver.onError(new RuntimeException("Cannot cancel completed or already cancelled order"));
//                                return;
//                            }
//
//                            // Update order status
//                            order.setStatus("CANCELLED");
//                            orderRepository.save(order);
//
//                            // Revert inventory
//                            List<ProductServiceClient.InventoryUpdate> inventoryUpdates = order.getOrderItems().stream()
//                                    .map(item -> new ProductServiceClient.InventoryUpdate(
//                                            item.getProductId(), -item.getQuantity()))
//                                    .collect(Collectors.toList());
//
//                            productServiceClient.updateInventory(inventoryUpdates);
//
//                            // Process refund if needed
//                            if (!order.getStatus().equals("PENDING")) {
//                                paymentServiceClient.processRefund(order.getOrderId(), order.getTotalAmount());
//                            }
//
//                            OrderResponse response = buildOrderResponse(order);
//                            responseObserver.onNext(response);
//                            responseObserver.onCompleted();
//                        },
//                        () -> responseObserver.onError(new RuntimeException("Order not found"))
//                );
//    }
//
//    private OrderResponse buildOrderResponse(Order order) {
//        return OrderResponse.newBuilder()
//                .setOrderId(order.getOrderId())
//                .setStatus(order.getStatus())
//                .setCreatedAt(order.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
//                .setTotalAmount(order.getTotalAmount())
//                .setEstimatedDelivery(order.getEstimatedDelivery() != null ?
//                        order.getEstimatedDelivery().format(DateTimeFormatter.ISO_DATE_TIME) : "")
//                .build();
//    }
//}
