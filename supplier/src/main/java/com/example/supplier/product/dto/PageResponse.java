package com.example.supplier.product.dto;

import java.util.List;

public record PageResponse<T>(int totalPages, long totalElements, int currentPage, int pageSize, List<T> content) {
}
