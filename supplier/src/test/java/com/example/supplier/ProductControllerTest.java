package com.example.supplier;

import com.example.supplier.category.Category;
import com.example.supplier.category.CategoryRepository;
import com.example.supplier.config.EnablePostgresTestContainerContextCustomizerFactory.EnabledPostgresTestContainer;
import com.example.supplier.product.Product;
import com.example.supplier.product.ProductRepository;
import com.example.supplier.product.dto.PageResponse;
import com.example.supplier.product.dto.ProductCreateDto;
import com.example.supplier.product.dto.ProductResponseDto;
import com.example.supplier.rating.Rating;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnabledPostgresTestContainer
@Slf4j
public class ProductControllerTest{

    @LocalServerPort
    Integer port;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private final String GET_PRODUCTS = "api/products";
    private final String CREATE_PRODUCT = "api/products";
    private final String GET_PRODUCT = "api/products/{id}";
    private final String DELETE_PRODUCT = "api/products/{id}";
    private final String FILTER_PRODUCTS = "api/products/filter";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        categoryRepository.deleteAll();
    }

    @Test
    public void testCreateProductWithExistedCategory() {
        log.info("Port: {}", port);
        Category category = createCategory("Electronics");
        ProductCreateDto productDto = createProductDto(
                "Laptop",
                1000L,
                "A laptop",
                category.getId());

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(productDto)
                .post(CREATE_PRODUCT);

        assertEquals(201, response.getStatusCode());

        ProductResponseDto productResponseDto = response.as(ProductResponseDto.class);
        assertEquals(productDto.getName(), productResponseDto.getName());
        assertEquals(productDto.getPrice(), productResponseDto.getPrice());
        assertEquals(productDto.getDescription(), productResponseDto.getDescription());
        assertEquals(productDto.getCategoryId(), productResponseDto.getCategoryId());
        assertEquals(0, productResponseDto.getAverageRating(), 0.01);
    }

    @Test
    public void testCreateProductWithNonExistedCategory() {
        log.info("Port: {}", port);
        ProductCreateDto productDto = createProductDto(
                "Laptop",
                1000L,
                "A laptop",
                1L);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(productDto)
                .post(CREATE_PRODUCT);

        assertEquals(404, response.getStatusCode());
    }




    @Test
    public void testGetExistedProductById() {
        Category category = createCategory("Electronics");
        Product product = createProduct(
                "Laptop",
                1000L,
                "A laptop",
                category,
                4.5f);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get(GET_PRODUCT, product.getId());

        assertEquals(200, response.getStatusCode());

        assertProductDtoEqualsProduct(response.as(ProductResponseDto.class), product);
    }

    @Test
    public void testGetNonExistedProductById() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get(GET_PRODUCT, 1L);

        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testDeleteCategory() {
        Category category = createCategory("Electronics");
        Product product = createProduct(
                "Laptop",
                1000L,
                "A laptop",
                category,
                4.5f);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .delete(DELETE_PRODUCT, product.getId());

        assertEquals(200, response.getStatusCode());
        // body is empty
        assertEquals("", response.getBody().asString());
    }

    @Test
    public void testDeleteNonExistedProduct() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .delete(DELETE_PRODUCT, 1L);

        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testFindAllProductsWithFilter() {
        // Create categories
        Category electronicsCategory = createCategory("Electronics");
        Category clothingCategory = createCategory("Clothing");

        // Create products
        Product laptopProduct = createProduct(
                "Laptop",
                1000L,
                "A laptop",
                electronicsCategory,
                4.5f);
        Product tshirtProduct = createProduct(
                "T-shirt",
                20L, "A t-shirt",
                clothingCategory,
                3.5f);

        // Filter by description
        testFilterByDescription("laptop", laptopProduct);

        // Filter by category
        testFilterByCategory(clothingCategory.getId(), tshirtProduct);

        // Filter by price
        testFilterByPrice(100, 1000, laptopProduct);

        // Filter by rating
        testFilterByRating(4.0f, 5.0f, laptopProduct);
    }

    private void testFilterByRating(float v, float v1, Product laptopProduct) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get("api/products/filter?minRating=" + v + "&maxRating=" + v1);

        assertEquals(200, response.getStatusCode());

        TypeRef<PageResponse<ProductResponseDto>> typeRef = new TypeRef<>() {
        };
        PageResponse<ProductResponseDto> pageResponse = response.as(typeRef);
        assertEquals(1, pageResponse.content().size());
        assertProductDtoEqualsProduct(pageResponse.content().get(0), laptopProduct);
    }

    private void testFilterByDescription(String description, Product expectedProduct) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get("api/products/filter?description=" + description);

        assertEquals(200, response.getStatusCode());

        TypeRef<PageResponse<ProductResponseDto>> typeRef = new TypeRef<>() {
        };
        PageResponse<ProductResponseDto> pageResponse = response.as(typeRef);
        assertEquals(1, pageResponse.content().size());
        assertProductDtoEqualsProduct(pageResponse.content().get(0), expectedProduct);
    }

    private void testFilterByCategory(Long categoryId, Product expectedProduct) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get("api/products/filter?categoryId=" + categoryId);

        assertEquals(200, response.getStatusCode());

        TypeRef<PageResponse<ProductResponseDto>> typeRef = new TypeRef<>() {
        };
        PageResponse<ProductResponseDto> pageResponse = response.as(typeRef);
        assertEquals(1, pageResponse.content().size());
        assertProductDtoEqualsProduct(pageResponse.content().get(0), expectedProduct);
    }

    private void testFilterByPrice(int minPrice, int maxPrice, Product expectedProduct) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get("api/products/filter?minPrice=" + minPrice + "&maxPrice=" + maxPrice);

        assertEquals(200, response.getStatusCode());

        TypeRef<PageResponse<ProductResponseDto>> typeRef = new TypeRef<>() {
        };
        PageResponse<ProductResponseDto> pageResponse = response.as(typeRef);
        assertEquals(1, pageResponse.content().size());
        assertProductDtoEqualsProduct(pageResponse.content().get(0), expectedProduct);
    }

    private void assertProductDtoEqualsProduct(ProductResponseDto productDto, Product product) {
        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getPrice(), productDto.getPrice());
        assertEquals(product.getDescription(), productDto.getDescription());
        assertEquals(product.getCategory().getId(), productDto.getCategoryId());
        assertEquals(product.getRating().getAverageRating(), productDto.getAverageRating(), 0.01);
    }


    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private ProductCreateDto createProductDto(String name, Long price, String description, Long categoryId) {
        ProductCreateDto productDto = new ProductCreateDto();
        productDto.setName(name);
        productDto.setPrice(price);
        productDto.setDescription(description);
        productDto.setCategoryId(categoryId);
        return productDto;
    }

    private Product createProduct(String name, Long price, String description, Category category, float averageRating) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCategory(category);
        Rating rating = new Rating();
        rating.setAverageRating(averageRating);
        product.setRating(rating);
        return productRepository.save(product);
    }
}
