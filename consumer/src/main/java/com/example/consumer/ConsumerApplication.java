package com.example.consumer;

import com.example.consumer.category.CategoryDto;
import com.example.consumer.category.ConsumerCategoryService;
import com.example.consumer.product.ConsumerProductService;
import com.example.consumer.product.ProductCreateDto;
import com.example.consumer.product.ProductResponseDto;
import com.example.consumer.product.ProductUpdateDto;
import com.example.consumer.review.ConsumerReviewService;
import com.example.consumer.review.ReviewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
@Slf4j
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(ConsumerCategoryService consumerCategoryService,
                                        ConsumerProductService consumerProductService,
                                        ConsumerReviewService consumerReviewService) {
        return args -> {

            // CATEGORY
            // GET
            log.info("Fetching category by id: 1, {}", consumerCategoryService.getCategoryById(1L));
            log.info("Fetching category by id: 2, {}", consumerCategoryService.getCategoryById(2L));
            log.info("Fetching category by id: 3, {}", consumerCategoryService.getCategoryById(3L));

            // CREATE
            CategoryDto categoryFromConsumer =
                    consumerCategoryService.createCategory(CategoryDto.createCategoryDto("Category from consumer"));

            log.info("Created category from consumer: {}", categoryFromConsumer);

            // UPDATE
            consumerCategoryService.updateCategory(categoryFromConsumer.id(),
                    CategoryDto.createCategoryDto(categoryFromConsumer.id(), "Updated category from consumer"));

            // PRODUCT
            // GET
            log.info("Fetching product by id: 1, {}", consumerProductService.getProductById(1L));
            log.info("Fetching product by id: 2, {}", consumerProductService.getProductById(2L));
            log.info("Fetching product by id: 3, {}", consumerProductService.getProductById(3L));

            // CREATE
            ProductResponseDto productFromConsumer = consumerProductService.createProduct(new ProductCreateDto(
                    categoryFromConsumer.id(),
                    "Product from consumer",
                    "Product description from consumer",
                    10000L));

            // FILTER
            log.info("Fetching all product with pagination and filter {}",
                    consumerProductService.getAllProductsWithFilter(
                            0, 10, "ASC", "id", null));

            log.info("Fetching all product with pagination and filter {}",
                    consumerProductService.getAllProductsWithFilter(
                            0, 10, "ASC", "id",
                            Map.of(
                                    "description", "laptop"
                            )));
            // UPDATE
            consumerProductService.updateProduct(new ProductUpdateDto(
                    productFromConsumer.getId(),
                    productFromConsumer.getCategoryId(),
                    "Updated product from consumer",
                    "Updated product description from consumer",
                    20000L
            ));

            // GET UPDATED
            log.info("Fetching product by id: {}, {}", productFromConsumer.getId(),
                    consumerProductService.getProductById(productFromConsumer.getId()));

            // DELETE
            consumerProductService.deleteProduct(productFromConsumer.getId());

            // GET DELETED
            log.info("Fetching product by id: {}, {}", productFromConsumer.getId(),
                    consumerProductService.getProductById(productFromConsumer.getId()));

            // REVIEW

            // CREATE
            ReviewDto reviewFromConsumer = consumerReviewService.createReview(1L, new ReviewDto(
                    null,
                    5,
                    "Review description from consumer",
                    1L
            ));

            ReviewDto reviewFromConsumer2 = consumerReviewService.createReview(1L, new ReviewDto(
                    null,
                    3,
                    "Review description from consumer",
                    1L
            ));

            ReviewDto reviewFromConsumer3 = consumerReviewService.createReview(1L, new ReviewDto(
                    null,
                    5,
                    "Review description from consumer",
                    1L
            ));

            // GET
            log.info("Fetching review by id: {}, {}", reviewFromConsumer2,
                    consumerReviewService.getReviewById(reviewFromConsumer2.getId()));
            // GET ALL
            log.info("Fetching all reviews by product id: {}, {}", reviewFromConsumer3.getProductId(),
                    consumerReviewService.getAllReviewsByProductId(reviewFromConsumer3.getProductId()));

            // Changed rating of the 1st product
            log.info("Fetching product by id: {}, {}", 1L,
                    consumerProductService.getProductById(1L));

            // UPDATE
            consumerReviewService.updateReview(reviewFromConsumer2.getId(), new ReviewDto(
                    reviewFromConsumer2.getId(),
                    4,
                    "Updated review description from consumer",
                    1L
            ));

            // GET Product with updated rating
            log.info("Fetching product by id: {}, {}", 1L,
                    consumerProductService.getProductById(1L));

        };
    }

}

