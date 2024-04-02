package com.example.supplier.product;

import com.example.supplier.product.dto.ProductResponseDto;
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
    private List<ProductResponseDto> products;
}
