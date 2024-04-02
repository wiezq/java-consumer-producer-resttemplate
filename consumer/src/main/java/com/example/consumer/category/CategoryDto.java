package com.example.consumer.category;

public record CategoryDto(Long id, String name) {

    public static CategoryDto createCategoryDto(Long id, String name) {
        return new CategoryDto(id, name);
    }

    public static CategoryDto createCategoryDto(String name) {
        return new CategoryDto(null, name);
    }

}
