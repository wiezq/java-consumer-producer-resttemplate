package com.example.supplier.dto.mapper;

import com.example.supplier.dto.ProductDto;
import com.example.supplier.dto.request.ProductFilterRequest;
import com.example.supplier.model.Category;
import com.example.supplier.model.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;


@Component
public class ProductMapper {

    public ProductDto mapToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setAverageRating(product.getAverageRating());
        return productDto;
    }

    public Pageable mapToPageRequest(Integer page, Integer size, String sort, String sortBy) {
        if (!sort.equalsIgnoreCase("ASC") && !sort.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("Sort direction must be 'ASC' or 'DESC'");
        }
        return PageRequest.of(page, size, Sort.Direction.fromString(sort), sortBy);
    }

    public Product mapToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setAverageRating(productDto.getAverageRating());

        Category category = new Category();
        category.setId(productDto.getCategoryId());
        product.setCategory(category);
        return product;
    }

    public ProductFilterRequest toProductFilterRequest(
            Long minPrice,
            Long maxPrice,
            Float minRating,
            Float maxRating,
            Long categoryId,
            String description
    ) {
        return ProductFilterRequest.builder()
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .minRating(minRating)
                .maxRating(maxRating)
                .categoryId(categoryId)
                .description(description)
                .build();
    }
}
