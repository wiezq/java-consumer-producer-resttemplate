package com.example.supplier.repository.specification;

import com.example.supplier.dto.request.ProductFilterRequest;
import com.example.supplier.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSpecification {

    public Specification<Product> build(ProductFilterRequest filterRequest) {
        List<Specification<Product>> specs = new ArrayList<>();

        if (filterRequest.getCategoryId() != null) {
            specs.add(hasCategoryId(filterRequest.getCategoryId()));
        }


        if (filterRequest.getMinPrice() <= filterRequest.getMaxPrice()) {
            specs.add(hasPriceBetween(filterRequest.getMinPrice(), filterRequest.getMaxPrice()));
        }


        if (filterRequest.getMinRating().compareTo(filterRequest.getMaxRating()) <= 0) {
            specs.add(hasRatingBetween(filterRequest.getMinRating(), filterRequest.getMaxRating()));
        }

        if (filterRequest.getDescription() != null) {
            specs.add(hasDescriptionLike(filterRequest.getDescription()));
        }
        return Specification.where(Specification.allOf(specs));
    }


    private Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    private Specification<Product> hasPriceBetween(Long minPrice, Long maxPrice) {
        return (root, query, cb) -> cb.between(root.get("price"), minPrice, maxPrice);
    }

    private Specification<Product> hasRatingBetween(Float minRating, Float maxRating) {
        return (root, query, cb) -> cb.between(root.get("averageRating"), minRating, maxRating);
    }

    private Specification<Product> hasDescriptionLike(String description) {
        return (root, query, cb) -> cb.like(root.get("description"), "%" + description + "%");
    }
}
