package com.example.consumer.product;

import lombok.Data;


@Data
public class ProductResponseDto {
    private Long id;
    private Long price;
    private Long categoryId;
    private Float averageRating;
    private String name;
    private String description;
}
