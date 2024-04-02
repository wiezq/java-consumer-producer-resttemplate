package com.example.consumer.product;

import lombok.Data;

@Data
public class ProductUpdateDto {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private Long price;

    public ProductUpdateDto(Long id, Long categoryId, String name, String description, Long price) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}

