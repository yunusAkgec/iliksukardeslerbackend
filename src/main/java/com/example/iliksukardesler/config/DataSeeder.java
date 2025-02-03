package com.example.iliksukardesler.config;

import com.example.iliksukardesler.model.Category;
import com.example.iliksukardesler.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepository){
        return args -> {
            if (categoryRepository.count()==0){
                for (Category.CategoryType type :Category.CategoryType.values()){
                    Category category = new Category();
                    category.setName(type);
                    categoryRepository.save(category);
                }
            }
        };
    }

}
