package com.example.iliksukardesler.repository;

import com.example.iliksukardesler.model.Cart;
import com.example.iliksukardesler.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
