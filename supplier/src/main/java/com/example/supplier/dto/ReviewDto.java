package com.example.supplier.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private Long id;
    private int rating;
    private String comment;
    private Long productId;
}
