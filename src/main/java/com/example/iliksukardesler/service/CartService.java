package com.example.iliksukardesler.service;

import com.example.iliksukardesler.model.Cart;
import com.example.iliksukardesler.model.Product;
import com.example.iliksukardesler.model.User;
import com.example.iliksukardesler.repository.CartRepository;
import com.example.iliksukardesler.repository.ProductRepository;
import com.example.iliksukardesler.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Cart addProductToCart(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new RuntimeException("Product not found")
                );
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> new Cart());
        cart.setUser(user);
        cart.getProducts().add(product);
        cart.setTotalPrice(cart.getTotalPrice().add(product.getPrice()));
        return cartRepository.save(cart);
    }

    public Cart removeProductFromCart(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new RuntimeException("Product not found")
                );
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(
                        () -> new RuntimeException("Cart not found")
                );

        if (cart.getProducts().remove(product)) {
            cart.setTotalPrice(cart.getTotalPrice().subtract(product.getPrice()));
            return cartRepository.save(cart);
        } else {
            throw new RuntimeException("Product not found in cart");
        }
    }

    public Cart getCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public void updateCart(Cart cart) {
        // Sepeti veri tabanında güncelle
        System.out.println("Updating Cart for user: " + cart.getUser().getUsername());
        cartRepository.save(cart);

    }
}
