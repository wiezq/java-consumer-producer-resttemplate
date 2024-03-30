package com.example.supplier.product;

import com.example.supplier.product.dto.ProductCreateDto;
import com.example.supplier.product.dto.PageResponse;
import com.example.supplier.product.dto.ProductResponseDto;
import com.example.supplier.product.dto.ProductUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductControllerImpl {

    private final ProductService productService;

    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateDto productDto) {
        Product product = productMapper.mapToProductEntity(productDto);
        Product createdProduct = productService.createProduct(product);
        ProductResponseDto productResponseDto = productMapper.mapToResponseProductDto(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDto);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getAllProductsWithPaginationAndFilter(@RequestParam Map<String, String> filterParams) {
        Pageable productPageRequest = productMapper.mapToPageRequest(filterParams);
        ProductFilterRequest productFilterRequest = productMapper.mapToProductFilterRequest(filterParams);
        Page<Product> allProductsByPage = productService.getAllProductsByPage(productPageRequest, productFilterRequest);
        PageResponse<ProductResponseDto> pageResponse = productMapper.mapToPageResponse(allProductsByPage);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productMapper.mapToResponseProductDto(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@RequestBody ProductUpdateDto productDto) {
        Product updatedProduct = productService.updateProduct(productDto);
        return productMapper.mapToResponseProductDto(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}