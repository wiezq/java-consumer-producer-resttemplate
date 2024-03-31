package com.example.consumer.product;

import java.util.List;

public record PageResponse<T>(int totalPages, long totalElements, int currentPage, int pageSize, List<T> content) {
}
