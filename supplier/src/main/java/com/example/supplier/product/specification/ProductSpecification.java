package com.example.supplier.product.specification;

import com.example.supplier.product.Product;
import com.example.supplier.product.ProductFilterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ProductSpecification {

    public Specification<Product> build(ProductFilterRequest filterRequest) {
        log.info("Building specification for product filter request: {}", filterRequest);
        List<Specification<Product>> specifications = new ArrayList<>();
        if (filterRequest.getCategoryId() != null) {
            specifications.add((root1, query1, cb1) -> cb1.equal(root1.get("category").get("id"), filterRequest.getCategoryId()));
        }
        if (filterRequest.getDescription() != null) {
            specifications.add((root1, query1, cb1) -> cb1.like(root1.get("description"), "%" + filterRequest.getDescription() + "%"));
        }
        if (filterRequest.getMinPrice() != null && filterRequest.getMaxPrice() != null) {
            specifications.add((root1, query1, cb1) -> cb1.between(root1.get("price"), filterRequest.getMinPrice(), filterRequest.getMaxPrice()));
        }
        if (filterRequest.getMinRating() != null && filterRequest.getMaxRating() != null) {
            specifications.add((root1, query1, cb1) -> cb1.between(root1.get("rating").get("averageRating"), filterRequest.getMinRating(), filterRequest.getMaxRating()));
        }
        Specification<Product> spec = Specification.allOf(specifications);
        log.info("Built specification: {}", spec);
        return spec;
    }
}
