package com.example.iliksukardesler.service;

import com.example.iliksukardesler.model.Cart;
import com.example.iliksukardesler.model.Order;
import com.example.iliksukardesler.model.Product;
import com.example.iliksukardesler.model.User;
import com.example.iliksukardesler.repository.OrderRepository;
import com.example.iliksukardesler.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartService cartService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    public Order createOrder(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not Found"));
        Cart cart = cartService.getCart(username);

        System.out.println("Cart for user: " + username + "--> Products: " + cart.getProducts());

        if (cart.getProducts().isEmpty()){
            throw new RuntimeException("Cart is empty. Cannot create order.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setProducts(new ArrayList<>(cart.getProducts()));
        order.setTotalPrice(cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add));
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        //Sepeti Temizle
        cart.getProducts().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartService.updateCart(cart);

        return orderRepository.save(order);


    }

    public List<Order> getUserOrders(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order getOrderById(Long orderId,String username){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUsername().equals(username)){
            throw new RuntimeException("Access Denied");
        }

        return order;
    }




}
