package com.example.supplier;

import com.example.supplier.category.Category;
import com.example.supplier.category.CategoryService;
import com.example.supplier.product.Product;
import com.example.supplier.product.ProductService;
import com.example.supplier.review.Review;
import com.example.supplier.review.ReviewService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class SupplierApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierApplication.class, args);
    }

    @Profile("!test")
    @Bean
    public CommandLineRunner runner(
            ProductService productService,
            CategoryService categoryService,
            ReviewService reviewService) {
        return args -> {
            Category c1 = new Category();
            c1.setName("Electronics");
            Category category = categoryService.createCategory(c1);

            Category c2 = new Category();
            c2.setName("Clothing");
            Category category2 = categoryService.createCategory(c2);

            Product p1 = new Product();
            p1.setName("Laptop");
            p1.setPrice(1000L);
            p1.setDescription("A laptop");
            p1.setCategory(category);
            Product product = productService.createProduct(p1);

            Product p2 = new Product();
            p2.setName("T-shirt");
            p2.setPrice(20L);
            p2.setDescription("A t-shirt");
            p2.setCategory(category2);
            Product product1 = productService.createProduct(p2);

            Review r1 = new Review();
            r1.setRating(4);
            r1.setComment("Good product");
            r1.setProduct(product);
            reviewService.createReview(product.getId(), r1);

            Review r2 = new Review();
            r2.setRating(5);
            r2.setComment("Great product");
            r2.setProduct(product1);
            reviewService.createReview(product1.getId(), r2);
        };
    }

}
