package com.example.consumer.product;

import lombok.Data;

@Data
public class ProductCreateDto {
    private Long categoryId;
    private String name;
    private String description;
    private Long price;

    public ProductCreateDto(Long categoryId, String name, String description, Long price) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}

