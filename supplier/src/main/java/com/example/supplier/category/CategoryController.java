package com.example.supplier.category;

import com.example.supplier.exception.ErrorDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryController {

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
            }
    )
    ResponseEntity<?> createCategory(CategoryDto categoryDto);

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
    ResponseEntity<?> getAllCategories();

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
                                    schema = @Schema(implementation = ErrorDetails.class)))
            }
    )
    ResponseEntity<?> getCategoryById(Long id);

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
                                    schema = @Schema(implementation = ErrorDetails.class)))
            }
    )
    ResponseEntity<?> updateCategory(Long id, CategoryDto categoryDto);

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
                                    schema = @Schema(implementation = ErrorDetails.class)))
            }
    )
    ResponseEntity<?> deleteCategory(Long id);
}
