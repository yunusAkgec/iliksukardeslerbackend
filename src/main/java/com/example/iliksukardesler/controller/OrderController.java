package com.example.iliksukardesler.controller;

import com.example.iliksukardesler.model.Order;
import com.example.iliksukardesler.service.JwtTokenProvider;
import com.example.iliksukardesler.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    public OrderController(OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromHeader(authHeader);
        Order createdOrder = orderService.createOrder(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(@RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromHeader(authHeader);
        List<Order> orders = orderService.getUserOrders(username);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam("status") Order.OrderStatus status) {
        System.out.println("Received orderID: " + orderId + ", Status: " + status);
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
    private String extractUsernameFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is invalid or missing");
        }
        String token = authHeader.replace("Bearer ", "").trim();
        return jwtTokenProvider.getUsernameFromToken(token);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer " , "");
        String username = jwtTokenProvider.getUsernameFromToken(token);
        Order order = orderService.getOrderById(orderId,username);
        return ResponseEntity.ok(order);
    }


}
