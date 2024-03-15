package com.example.consumer.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class ProductResponseDto {
    private List<ProductDto> content;
    private PageableDto pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private int size;
    private int number;
    private SortDto sort;
    private int numberOfElements;
    private boolean empty;

}