package com.example.supplier.controller;

import com.example.supplier.dto.CategoryDto;
import com.example.supplier.model.Category;
import com.example.supplier.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/categories", produces = "application/json")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Create category",
            description = "Create category",
            tags = {"categories"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request, validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))

            }
    )
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = mapToEntity(categoryDto);
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToDto(createdCategory));
    }

    @Operation(
            summary = "Get all categories",
            description = "Get all categories",
            tags = {"categories"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All categories",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryDto.class))),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No content",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))
            }
    )

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<Category> allCategories = categoryService.getAllCategories();
        if (allCategories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(allCategories.stream()
                .map(this::mapToDto)
                .toList());
    }


    @Operation(
            summary = "Get category by id",
            description = "Get category by id",
            tags = {"categories"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable @Min(1) Long id) {
        return ResponseEntity
                .ok()
                .body(mapToDto(categoryService.getCategoryById(id)));
    }

    @Operation(
            summary = "Update category",
            description = "Update category",
            tags = {"categories"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable @Min(1) Long id, @RequestBody CategoryDto categoryDto) {
        Category category = mapToEntity(categoryDto);
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity
                .ok()
                .body(mapToDto(updatedCategory));
    }


    @Operation(
            summary = "Delete category",
            description = "Delete category by id",
            tags = {"categories"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable @Min(1) Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    private Category mapToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}