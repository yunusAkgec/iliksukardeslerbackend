package com.example.iliksukardesler.repository;

import com.example.iliksukardesler.model.Order;
import com.example.iliksukardesler.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);
}
