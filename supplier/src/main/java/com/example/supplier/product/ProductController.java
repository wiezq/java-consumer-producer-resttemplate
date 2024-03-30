package com.example.supplier.product;

import com.example.supplier.product.dto.ProductCreateDto;
import com.example.supplier.product.dto.ProductResponseDto;
import com.example.supplier.product.dto.ProductUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
                            responseCode = "200",
                            description = "Created product",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)))
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
                                    schema = @Schema(implementation = ProductResponseDto.class)))
            }
    )
    ResponseEntity<?> getAllProductsWithPaginationAndFilter(@RequestParam Map<String, String> filterParams);

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
                                    schema = @Schema(implementation = ProductResponseDto.class)))
            }
    )
    ProductResponseDto getProductById(Long id);

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
                                    schema = @Schema(implementation = ProductResponseDto.class)))
            }
    )
    ProductResponseDto updateProduct(ProductUpdateDto productDto);

    @Operation(
            summary = "Delete product",
            description = "Delete product",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted product",
                            content = @Content(
                                    mediaType = "application/json"))
            }
    )
    ResponseEntity<Void> deleteProduct(Long id);
}
