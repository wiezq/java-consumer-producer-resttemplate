package com.example.supplier.product;

import com.example.supplier.exception.ErrorDetails;
import com.example.supplier.product.dto.ProductCreateDto;
import com.example.supplier.product.dto.ProductResponseDto;
import com.example.supplier.product.dto.ProductUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface ProductController {
    @Operation(
            summary = "Create product",
            description = "Create product",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created product",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)))
            }
    )
    ResponseEntity<ProductResponseDto> createProduct(ProductCreateDto productDto);

    @Operation(
            summary = "Get all products with pagination and filter",
            description = "Get all products with pagination and filter",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Fetched products",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request, validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)))
            },
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Number of elements per page", example = "10"),
                    @Parameter(name = "sortDir", description = "Sort direction. ASC or DESC", example = "asc"),
                    @Parameter(name = "sortBy", description = "Sort by. Name, price, rating, id", example = "id"),

                    @Parameter(name = "description", description = "Product description"),
                    @Parameter(name = "categoryId", description = "Category id"),
                    @Parameter(name = "minPrice", description = "Minimum price", example = "0"),
                    @Parameter(name = "maxPrice", description = "Maximum price", example = "100000000"),
                    @Parameter(name = "minRating", description = "Minimum rating", example = "1"),
                    @Parameter(name = "maxRating", description = "Maximum rating", example = "5")
            }
    )
    ResponseEntity<?> getAllProductsWithPaginationAndFilter(
            @Parameter(hidden = true)
            @RequestParam Map<String, String> filterParams);

    @Operation(
            summary = "Get product by id",
            description = "Get product by id",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Fetched product",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)))
            }

    )
    ResponseEntity<?> getProductById(Long id);

    @Operation(
            summary = "Update product",
            description = "Update product",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated product",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDetails.class)))
            }
    )
    ResponseEntity<?> updateProduct(ProductUpdateDto productDto);

    @Operation(
            summary = "Delete product",
            description = "Delete product",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted product",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            ))
            }
    )
    ResponseEntity<Void> deleteProduct(Long id);
}
