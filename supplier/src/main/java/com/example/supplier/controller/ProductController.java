package com.example.supplier.controller;

import com.example.supplier.dto.ProductDto;
import com.example.supplier.dto.ProductPageResponse;
import com.example.supplier.dto.mapper.ProductMapper;
import com.example.supplier.dto.request.ProductFilterRequest;
import com.example.supplier.model.Product;
import com.example.supplier.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

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
                                    schema = @Schema(implementation = ProductDto.class)))
            }
    )
    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        Product product = productMapper.mapToEntity(productDto);
        Product createdProduct = productService.createProduct(product);
        return productMapper.mapToDto(createdProduct);
    }


    @Operation(
            summary = "Get all products with pagination, sorting and filtering",
            description = "Get all products with pagination, sorting and filtering",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "List of products",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductPageResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid page, size, sort or sortBy",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @Parameter(description = "Page number")
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be less than 0")
            Integer page,

            @Parameter(description = "Page size")
            @RequestParam(required = false, defaultValue = "10")
            @Min(value = 1, message = "Page size cannot be less than 1")
            Integer size,

            @Parameter(description = "Sort direction")
            @RequestParam(required = false, defaultValue = "ASC")
            String sort,

            @Parameter(description = "Sort field")
            @RequestParam(required = false, defaultValue = "id")
            String sortBy,

            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "minPrice must be greater than or equal to 0")
            @Max(value = Long.MAX_VALUE, message = "minPrice must be less than or equal to " + Long.MAX_VALUE)
            Long minPrice,

            @RequestParam(required = false, defaultValue = Long.MAX_VALUE + "")
            @Min(value = 0, message = "maxPrice must be greater than or equal to 0")
            @Max(value = Long.MAX_VALUE, message = "maxPrice must be less than or equal to " + Long.MAX_VALUE)
            Long maxPrice,

            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "minRating must be greater than or equal to 0")
            @Max(value = 5, message = "minRating must be less than or equal to 5")
            Float minRating,

            @RequestParam(required = false, defaultValue = "5")
            @Min(value = 0, message = "maxRating must be greater than or equal to 0")
            @Max(value = 5, message = "maxRating must be less than or equal to 5")
            Float maxRating,

            @RequestParam(required = false)
            @Min(value = 1, message = "categoryId must be greater than or equal to 1")
            Long categoryId,

            @RequestParam(required = false)
            @Size
            String description)

    {
        // Page request
        Pageable productPageRequest = productMapper.mapToPageRequest(page, size, sort, sortBy);
        ProductFilterRequest productFilterRequest = productMapper.toProductFilterRequest(
                minPrice, maxPrice, minRating, maxRating, categoryId, description
        );
        // Get all products by page
        Page<Product> allProductsByPage = productService
                .getAllProductsByPage(productPageRequest, productFilterRequest);

        return ResponseEntity.ok(allProductsByPage.map(productMapper::mapToDto));
    }


    @Operation(
            summary = "Get product by id",
            description = "Get product by id",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productMapper.mapToDto(productService.getProductById(id));
    }


    @Operation(
            summary = "Update product",
            description = "Update product",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated product",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    schema = @Schema()))
            }
    )
    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product product = productMapper.mapToEntity(productDto);
        Product updatedProduct = productService.updateProduct(id, product);
        return productMapper.mapToDto(updatedProduct);
    }


    @Operation(
            summary = "Delete product",
            description = "Delete product by id",
            tags = {"products"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product deleted"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    schema = @Schema()))
            }
    )
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}