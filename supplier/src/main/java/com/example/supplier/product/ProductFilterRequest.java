package com.example.supplier.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductFilterRequest {
    private Long categoryId;
    private Long minPrice;
    private Long maxPrice;
    private Float minRating;
    private Float maxRating;
    private String description;
}
