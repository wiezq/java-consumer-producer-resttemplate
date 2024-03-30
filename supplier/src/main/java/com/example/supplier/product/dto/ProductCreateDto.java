package com.example.supplier.product.dto;

import lombok.Data;

@Data
public class ProductCreateDto {
    private Long categoryId;
    private String name;
    private String description;
    private Long price;
}
