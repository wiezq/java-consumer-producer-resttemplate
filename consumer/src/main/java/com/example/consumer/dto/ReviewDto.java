package com.example.consumer.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDto {
    private Long id;
    private int rating;
    private String comment;
    private Long productId;
}
