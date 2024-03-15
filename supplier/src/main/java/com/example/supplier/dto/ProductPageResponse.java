package com.example.supplier.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageResponse {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private List<ProductDto> products;
}
