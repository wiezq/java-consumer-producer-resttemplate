package com.example.supplier.product;

import com.example.supplier.category.Category;
import com.example.supplier.product.dto.ProductCreateDto;
import com.example.supplier.product.dto.PageResponse;
import com.example.supplier.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductMapper {
    public Product mapToProductEntity(ProductCreateDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        Category category = new Category();
        category.setId(productDto.getCategoryId());
        product.setCategory(category);
        return product;
    }

    public ProductResponseDto mapToResponseProductDto(Product createdProduct) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(createdProduct.getId());
        productResponseDto.setName(createdProduct.getName());
        productResponseDto.setPrice(createdProduct.getPrice());
        productResponseDto.setDescription(createdProduct.getDescription());
        productResponseDto.setCategoryId(createdProduct.getCategory().getId());
        productResponseDto.setAverageRating(createdProduct.getRating().getAverageRating());
        return productResponseDto;
    }


    // Map filter parameters to ProductFilterRequest and validate them
    public ProductFilterRequest mapToProductFilterRequest(Map<String, String> filterParams) {
        long minPrice = Long.parseLong(filterParams.getOrDefault("minPrice", "0"));
        if (minPrice < 0) {
            throw new IllegalArgumentException("minPrice must be greater than or equal to 0");
        }

        long maxPrice = Long.parseLong(filterParams.getOrDefault("maxPrice", Long.MAX_VALUE + ""));
        if (maxPrice < 0) {
            throw new IllegalArgumentException("maxPrice must lower than or equal to " + Long.MAX_VALUE);
        }

        float minRating = Float.parseFloat(filterParams.getOrDefault("minRating", "0"));
        if (minRating < 0 || minRating > 5) {
            throw new IllegalArgumentException("minRating must be between 0 and 5");
        }

        float maxRating = Float.parseFloat(filterParams.getOrDefault("maxRating", "5"));
        if (maxRating < 0 || maxRating > 5 || maxRating < minRating) {
            throw new IllegalArgumentException("maxRating must be between 0 and 5");
        }

        Long categoryId;

        String categoryId1 = filterParams.get("categoryId");
        if (categoryId1 != null) {
            categoryId = Long.parseLong(categoryId1);
        } else {
            categoryId = null;
        }

        String description = filterParams.get("description");

        return ProductFilterRequest.builder()
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .minRating(minRating)
                .maxRating(maxRating)
                .categoryId(categoryId)
                .description(description)
                .build();
    }


    // Map filter parameters to Pageable and validate them
    public Pageable mapToPageRequest(Map<String, String> filterParams) {
        int page = Integer.parseInt(filterParams.getOrDefault("page", "0"));
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be less than 0");
        }

        int size = Integer.parseInt(filterParams.getOrDefault("size", "10"));
        if (size < 1) {
            throw new IllegalArgumentException("Page size cannot be less than 1");
        }

        String sort = filterParams.getOrDefault("sortDir", "ASC");
        if (!sort.equalsIgnoreCase("ASC") && !sort.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("Sort direction must be 'ASC' or 'DESC'");
        }

        String sortBy = filterParams.getOrDefault("sortBy", "id");
        return PageRequest.of(page, size, Sort.Direction.fromString(sort), sortBy);
    }

    public PageResponse<ProductResponseDto> mapToPageResponse(Page<Product> allProductsByPage) {
        return new PageResponse<>(
                allProductsByPage.getTotalPages(),
                allProductsByPage.getTotalElements(),
                allProductsByPage.getNumber(),
                allProductsByPage.getSize(),
                allProductsByPage.map(this::mapToResponseProductDto).getContent());
    }
}
