package br.unifor.order_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Service
public class PaymentServiceClient {

    @Value("${services.payment.url}")
    private String paymentServiceUrl;

    private final RestTemplate restTemplate;

    public PaymentServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean processPayment(String userId, String paymentMethodId, double amount) {
        String url = paymentServiceUrl + "/api/payments/process";

        Map<String, Object> request = Map.of(
                "userId", userId,
                "paymentMethodId", paymentMethodId,
                "amount", amount
        );

        Map<String, Object> response = restTemplate.postForObject(
                url, request, Map.class);

        return response != null && Boolean.TRUE.equals(response.getOrDefault("success", false));
    }

    public boolean processRefund(String orderId, double amount) {
        String url = paymentServiceUrl + "/api/payments/refund";

        Map<String, Object> request = Map.of(
                "orderId", orderId,
                "amount", amount
        );

        Map<String, Object> response = restTemplate.postForObject(
                url, request, Map.class);

        return response != null && Boolean.TRUE.equals(response.getOrDefault("success", false));
    }
}
