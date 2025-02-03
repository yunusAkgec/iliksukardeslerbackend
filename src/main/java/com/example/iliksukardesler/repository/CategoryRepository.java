package com.example.iliksukardesler.repository;

import com.example.iliksukardesler.model.Category;
import com.example.iliksukardesler.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByName(Category.CategoryType name);
}
