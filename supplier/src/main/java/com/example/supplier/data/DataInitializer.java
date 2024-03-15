package com.example.supplier.data;

import com.example.supplier.model.Category;
import com.example.supplier.model.Product;
import com.example.supplier.model.Review;
import com.example.supplier.service.CategoryService;
import com.example.supplier.service.ProductService;
import com.example.supplier.service.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ReviewService reviewService;

    public DataInitializer(CategoryService categoryService, ProductService productService, ReviewService reviewService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initData();
    }

    private void initData() {
        List<Category> categories = createCategories();
        categories.forEach(category -> {
            Category savedCategory = categoryService.createCategory(category);
            List<Product> products = createProducts(savedCategory);
            products.forEach(product -> {
                Product savedProduct = productService.createProduct(product);
                List<Review> reviews = createReviews(savedProduct);
                reviews.forEach(review -> reviewService.createReview(savedProduct.getId(), review));
            });
        });
    }

    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Electronics"));
        categories.add(new Category("Books"));
        categories.add(new Category("Clothing"));
        categories.add(new Category("Home & Kitchen"));
        categories.add(new Category("Sports & Outdoors"));
        return categories;
    }

    private List<Product> createProducts(Category category) {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Product product = new Product();
            product.setName("Product " + i + " in " + category.getName());
            product.setPrice(new Random().nextLong(1000, 1000000L));
            product.setDescription("This is a product description.");
            product.setCategory(category);
            products.add(product);
        }
        return products;
    }

    private List<Review> createReviews(Product product) {
        List<Review> reviews = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Review review = new Review();
            review.setRating(new Random().nextInt(6));
            review.setComment("This is a review comment " + i + " for " + product.getName());
            review.setProduct(product);
            reviews.add(review);
        }
        return reviews;
    }
}
