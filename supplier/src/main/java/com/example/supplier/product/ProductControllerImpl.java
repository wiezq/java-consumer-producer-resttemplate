package com.example.supplier.product;

import com.example.supplier.product.dto.PageResponse;
import com.example.supplier.product.dto.ProductCreateDto;
import com.example.supplier.product.dto.ProductResponseDto;
import com.example.supplier.product.dto.ProductUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductControllerImpl implements ProductController{

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
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        ProductResponseDto productResponseDto = productMapper.mapToResponseProductDto(productService.getProductById(id));
        return ResponseEntity.ok().body(productResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductUpdateDto productDto) {
        Product updatedProduct = productService.updateProduct(productDto);
        ProductResponseDto responseDto = productMapper.mapToResponseProductDto(updatedProduct);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}