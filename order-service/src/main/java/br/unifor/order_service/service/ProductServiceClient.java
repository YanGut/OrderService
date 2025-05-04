//package br.unifor.order.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class ProductServiceClient {
//
//    @Value("${services.product.url}")
//    private String productServiceUrl;
//
//    private final RestTemplate restTemplate;
//
//    public ProductServiceClient(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public static class InventoryUpdate {
//        private String productId;
//        private int quantityChange;
//
//        public InventoryUpdate(String productId, int quantityChange) {
//            this.productId = productId;
//            this.quantityChange = quantityChange;
//        }
//
//        // Getters and setters
//        // ...
//    }
//
//    public boolean checkProductsAvailability(List<String> productIds) {
//        // Using REST to communicate with Product Service
//        String url = productServiceUrl + "/api/products/check-availability";
//        Map<String, Object> request = Map.of("productIds", productIds);
//
//        Map<String, Boolean> response = restTemplate.postForObject(
//                url, request, Map.class);
//
//        return response != null && response.getOrDefault("available", false);
//    }
//
//    public void updateInventory(List<InventoryUpdate> updates) {
//        String url = productServiceUrl + "/api/products/update-inventory";
//        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(updates), Void.class);
//    }
//}
