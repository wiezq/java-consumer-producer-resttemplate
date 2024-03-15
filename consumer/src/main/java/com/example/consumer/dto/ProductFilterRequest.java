package com.example.consumer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterRequest {
    private Long categoryId;
    private Long minPrice;
    private Long maxPrice;
    private Float minRating;
    private Float maxRating;
    private String description;
}