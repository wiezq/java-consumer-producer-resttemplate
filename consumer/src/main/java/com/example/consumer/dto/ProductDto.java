package com.example.consumer.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDto {
    private Long id;
    private Long price;
    private Long categoryId;
    private Float averageRating;
    private String name;
    private String description;
}
