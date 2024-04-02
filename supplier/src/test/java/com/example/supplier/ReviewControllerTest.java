package com.example.supplier;


import com.example.supplier.category.Category;
import com.example.supplier.category.CategoryRepository;
import com.example.supplier.config.EnablePostgresTestContainerContextCustomizerFactory.EnabledPostgresTestContainer;
import com.example.supplier.product.Product;
import com.example.supplier.product.ProductRepository;
import com.example.supplier.rating.Rating;
import com.example.supplier.review.ReviewDto;
import com.example.supplier.review.ReviewRepository;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnabledPostgresTestContainer
@Slf4j
public class ReviewControllerTest {
    @LocalServerPort
    private Integer port;


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        categoryRepository.deleteAll();
    }

    @Test
    public void testCreateReviewToExistedProduct() {
        Category category = createCategory("Electronics");
        Product product = createProduct("Laptop", 1000L, "A laptop", category);
        ReviewDto reviewDto = createReviewDto(4, "Good product", product.getId());
        RestAssured.given()
                .contentType("application/json")
                .body(reviewDto)
                .post("/api/reviews/products/{productId}", product.getId())
                .then()
                .statusCode(201)
                .body("rating", equalTo(reviewDto.getRating()))
                .body("comment", equalTo(reviewDto.getComment()))
                .body("productId", equalTo(reviewDto.getProductId().intValue()));
    }

    @Test
    public void testCreateReviewToNonExistedProduct() {
        ReviewDto reviewDto = createReviewDto(4, "Good product", 1L);
        RestAssured.given()
                .contentType("application/json")
                .body(reviewDto)
                .post("/api/reviews/products/{productId}", reviewDto.getProductId())
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetExistedReviewByCategoryId() {
        Category category = createCategory("Electronics");
        Product product = createProduct("Laptop", 1000L, "A laptop", category);
        ReviewDto reviewDto = createReviewDto(4, "Good product", product.getId());
        int reviewId = RestAssured.given()
                .contentType("application/json")
                .body(reviewDto)
                .post("/api/reviews/products/{productId}", product.getId())
                .then()
                .statusCode(201).extract().path("id");
        RestAssured.given()
                .contentType("application/json")
                .get("/api/reviews/{id}", reviewId)
                .then()
                .statusCode(200)
                .body("id", equalTo(reviewId))
                .body("rating", equalTo(reviewDto.getRating()))
                .body("comment", equalTo(reviewDto.getComment()))
                .body("productId", equalTo(reviewDto.getProductId().intValue()));
    }

    @Test
    public void testGetNonExistedReviewByCategoryId() {
        RestAssured.given()
                .contentType("application/json")
                .get("/api/reviews/{id}", 1)
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetReviewsByProductId(){
        Category category = createCategory("Electronics");
        Product product = createProduct("Laptop", 1000L, "A laptop", category);

        // Create 2 reviews
        ReviewDto reviewDto = createReviewDto(4, "Good product", product.getId());
        RestAssured.given()
                .contentType("application/json")
                .body(reviewDto)
                .post("/api/reviews/products/{productId}", product.getId())
                .then()
                .statusCode(201);

        ReviewDto reviewDto2 = createReviewDto(5, "Great product", product.getId());
        RestAssured.given()
                .contentType("application/json")
                .body(reviewDto2)
                .post("/api/reviews/products/{productId}", product.getId())
                .then()
                .statusCode(201);


        // Get reviews
        RestAssured.given()
                .contentType("application/json")
                .get("/api/reviews/products/{productId}", product.getId())
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].rating", equalTo(reviewDto.getRating()))
                .body("[0].comment", equalTo(reviewDto.getComment()))
                .body("[0].productId", equalTo(reviewDto.getProductId().intValue()))
                .body("[1].rating", equalTo(reviewDto2.getRating()))
                .body("[1].comment", equalTo(reviewDto2.getComment()))
                .body("[1].productId", equalTo(reviewDto2.getProductId().intValue()));

    }

    @Test
    public void testGetReviewsByNonExistedProductId(){
        RestAssured.given()
                .contentType("application/json")
                .get("/api/reviews/products/{productId}", 1)
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdateExistedReview(){
        Category category = createCategory("Electronics");
        Product product = createProduct("Laptop", 1000L, "A laptop", category);
        ReviewDto reviewDto = createReviewDto(4, "Good product", product.getId());
        int reviewId = RestAssured.given()
                .contentType("application/json")
                .body(reviewDto)
                .post("/api/reviews/products/{productId}", product.getId())
                .then()
                .statusCode(201).extract().path("id");

        ReviewDto updatedReviewDto = createReviewDto(5, "Great product", product.getId());
        RestAssured.given()
                .contentType("application/json")
                .body(updatedReviewDto)
                .put("/api/reviews/{id}", reviewId)
                .then()
                .statusCode(200)
                .body("rating", equalTo(updatedReviewDto.getRating()))
                .body("comment", equalTo(updatedReviewDto.getComment()))
                .body("productId", equalTo(updatedReviewDto.getProductId().intValue()));
    }

    @Test
    public void testUpdateNonExistedReview(){
        ReviewDto updatedReviewDto = createReviewDto(5, "Great product", 1L);
        RestAssured.given()
                .contentType("application/json")
                .body(updatedReviewDto)
                .put("/api/reviews/{id}", updatedReviewDto.getProductId())
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteExistedReview(){
        Category category = createCategory("Electronics");
        Product product = createProduct("Laptop", 1000L, "A laptop", category);
        ReviewDto reviewDto = createReviewDto(4, "Good product", product.getId());

        // extract id from response
        int reviewId = RestAssured.given()
                .contentType("application/json")
                .body(reviewDto)
                .post("/api/reviews/products/{productId}", product.getId())
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given()
                .contentType("application/json")
                .delete("/api/reviews/{id}", reviewId)
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteNonExistedReview(){
        RestAssured.given()
                .contentType("application/json")
                .delete("/api/reviews/{id}", 1)
                .then()
                .statusCode(404);
    }

    private Product createProduct(String name, Long price, String description, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCategory(category);
        Rating rating = new Rating();
        product.setRating(rating);
        return productRepository.save(product);
    }

    private ReviewDto createReviewDto(int rating, String comment, Long productId) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setRating(rating);
        reviewDto.setComment(comment);
        reviewDto.setProductId(productId);
        return reviewDto;
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
}
