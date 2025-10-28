package com.example.be.controller;

import com.example.be.dto.OrderRequestDTO;
import com.example.be.dto.OrderResponseDTO;
import com.example.be.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final OrderService orderService;

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Map<String, Object> createPayment(@RequestBody OrderRequestDTO request) {
        OrderResponseDTO order = orderService.createOrder(request);

        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("paymentUrl", frontendUrl + "/payment-gateway/" + order.getOrderCode());
        return response;
    }
}
