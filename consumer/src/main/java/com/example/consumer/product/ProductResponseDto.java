package com.example.consumer.product;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Data
public class ProductResponseDto {
    private Long id;
    private Long price;
    private Long categoryId;
    private Float averageRating;
    private String name;
    private String description;
}
