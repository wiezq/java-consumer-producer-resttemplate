package com.example.supplier.controller;

import com.example.supplier.dto.ProductDto;
import com.example.supplier.model.Category;
import com.example.supplier.model.Product;
import com.example.supplier.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        Product product = mapToEntity(productDto);
        Product createdProduct = productService.createProduct(product);
        return mapToDto(createdProduct);
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(this::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return mapToDto(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product product = mapToEntity(productDto);
        Product updatedProduct = productService.updateProduct(id, product);
        return mapToDto(updatedProduct);
    }

    private Product mapToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setAverageRating(BigDecimal.ZERO);

        Category category = new Category();
        category.setId(productDto.getCategoryId());
        product.setCategory(category);
        return product;
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    private ProductDto mapToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setAverageRating(product.getAverageRating());
        return productDto;
    }
}