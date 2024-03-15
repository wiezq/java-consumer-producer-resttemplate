package com.example.supplier.service;

import com.example.supplier.dto.request.ProductFilterRequest;
import com.example.supplier.exception.ResourceNotFoundException;
import com.example.supplier.model.Product;
import com.example.supplier.repository.ProductRepository;
import com.example.supplier.repository.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductSpecification productSpecification;

    public ProductService(ProductRepository productRepository, ProductSpecification productSpecification) {
        this.productRepository = productRepository;
        this.productSpecification = productSpecification;
    }

    public Product createProduct(Product product) {
        product.setAverageRating(0.0f);
        product.setTotalReviews(0);
        product.setTotalRating(0L);
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

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public Page<Product> getAllProductsByPage(Pageable productPageRequest, ProductFilterRequest productFilterRequest) {

        if (productFilterRequest == null) {
            return productRepository.findAll(productPageRequest);
        }
        Specification<Product> specification = productSpecification.build(productFilterRequest);

        return productRepository.findAll(specification, productPageRequest);


    }
}