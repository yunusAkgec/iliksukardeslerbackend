package com.example.iliksukardesler.mapper;

import com.example.iliksukardesler.dto.ProductDTO;
import com.example.iliksukardesler.model.Category;
import com.example.iliksukardesler.model.Product;
import com.example.iliksukardesler.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    public ProductMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        if (productDTO.getCategoryName() != null) {
            Category category = categoryRepository.findByName(Category.CategoryType.valueOf(productDTO.getCategoryName()))
                    .orElseThrow(() -> new RuntimeException("Category not found: " + productDTO.getCategoryName()));
            product.setCategory(category);
        }
        return product;
    }

    public ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStockQuantity(product.getStockQuantity());
        if (product.getCategory() != null) {
            productDTO.setCategoryName(product.getCategory().getName().name());
        }
        return productDTO;
    }
}