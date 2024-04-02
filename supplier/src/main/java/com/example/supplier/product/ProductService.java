package com.example.supplier.product;

import com.example.supplier.category.CategoryService;
import com.example.supplier.exception.ResourceNotFoundException;
import com.example.supplier.product.dto.ProductUpdateDto;
import com.example.supplier.product.specification.ProductSpecification;
import com.example.supplier.rating.Rating;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductSpecification productSpecification;

    private final CategoryService categoryService;


    public Product createProduct(Product product) {
        categoryService.getCategoryById(product.getCategory().getId());
        product.setRating(new Rating());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Product not found"));
    }

    public Product updateProduct(ProductUpdateDto product) {
        Product existingProduct = getProductById(product.getId());
        if (product.getName() != null) existingProduct.setName(product.getName());
        if (product.getPrice() != null) existingProduct.setPrice(product.getPrice());
        if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if (product.getCategoryId() != null){
            existingProduct.setCategory(categoryService.getCategoryById(product.getCategoryId()));
        }
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public Page<Product> getAllProductsByPage(Pageable productPageRequest, ProductFilterRequest productFilterRequest) {
        Specification<Product> specification = productSpecification.build(productFilterRequest);
        return productRepository.findAll(specification, productPageRequest);
    }
}