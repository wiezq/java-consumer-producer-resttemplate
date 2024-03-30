package com.example.supplier;

import com.example.supplier.category.Category;
import com.example.supplier.category.CategoryDto;
import com.example.supplier.category.CategoryRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class CategoryControllerTest extends BaseControllerTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private final String GET_CATEGORIES = "api/categories";
    private final String CREATE_CATEGORY = "api/categories";
    private final String GET_CATEGORY = "api/categories/{id}";
    private final String UPDATE_CATEGORY = "api/categories/{id}";
    private final String DELETE_CATEGORY = "api/categories/{id}";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        categoryRepository.deleteAll();
    }

    @Test
    public void testCreateCategory() {
        CategoryDto categoryDto = createCategoryDto("Electronics");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(categoryDto)
                .post(CREATE_CATEGORY)
                .then()
                .statusCode(201)
                .body("name", equalTo(categoryDto.getName()))
                .body("id", notNullValue());
        ;
    }

    @Test
    public void testGetExistedCategory() {
        Category category = createCategory("Electronics");
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get(GET_CATEGORY, category.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo(category.getName()))
                .body("id", equalTo(category.getId().intValue()));
    }

    @Test
    public void testGetNotExistedCategory() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get(GET_CATEGORY, 1)
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetCategories() {
        Category category = createCategory("Electronics");
        Category category1 = createCategory("Books");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get(GET_CATEGORIES)
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].name", equalTo(category.getName()))
                .body("[0].id", equalTo(category.getId().intValue()))
                .body("[1].name", equalTo(category1.getName()))
                .body("[1].id", equalTo(category1.getId().intValue()));
    }

    @Test
    public void testGetCategoriesEmpty() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get(GET_CATEGORIES)
                .then()
                .statusCode(204);
    }

    @Test
    public void testUpdateExistedCategory() {
        Category category = createCategory("Electronics");
        CategoryDto categoryDto = createCategoryDto("Books");
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(categoryDto)
                .put(UPDATE_CATEGORY, category.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo(categoryDto.getName()))
                .body("id", equalTo(category.getId().intValue()));
    }

    @Test
    public void testUpdateNotExistedCategory() {
        CategoryDto categoryDto = createCategoryDto("Books");
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(categoryDto)
                .put(UPDATE_CATEGORY, 1)
                .then()
                .statusCode(404);
    }

    @Test
    void testDeleteExistedCategory() {
        Category category = createCategory("Electronics");
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .delete(DELETE_CATEGORY, category.getId())
                .then()
                .statusCode(200);
    }

    @Test
    void testDeleteNotExistedCategory() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .delete(DELETE_CATEGORY, 1)
                .then()
                .statusCode(404);
    }

    private CategoryDto createCategoryDto(String name) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        return categoryDto;
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }


}
