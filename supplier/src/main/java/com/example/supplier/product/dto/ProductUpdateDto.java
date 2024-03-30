package com.example.supplier.product.dto;

import lombok.Data;

@Data
public class ProductUpdateDto {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private Long price;
}
