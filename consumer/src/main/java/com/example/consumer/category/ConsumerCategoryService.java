package com.example.consumer.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ConsumerCategoryService {

    private final RestTemplate restTemplate;

    @Value("${api.url}")
    private String supplierUrl;

    public ConsumerCategoryService() {
        this.restTemplate = new RestTemplate();
    }


    public CategoryDto getCategoryById(Long id) {
        try {
            String url = supplierUrl + "/api/categories/" + id;
            ResponseEntity<CategoryDto> response = restTemplate.getForEntity(url, CategoryDto.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            // Handle the exception
            log.error("Error occurred while fetching category by id: {}", e.getMessage());
            return null;
        }
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        try {
            String url = supplierUrl + "/api/categories";
            ResponseEntity<CategoryDto> response = restTemplate.postForEntity(url, categoryDto, CategoryDto.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            // Handle the exception
            log.error("Error occurred while creating category: {}", e.getMessage());
            return null;
        }
    }

    public void updateCategory(Long id, CategoryDto categoryDto) {
        try {
            String url = supplierUrl + "/api/categories/" + id;
            HttpEntity<CategoryDto> requestEntity = new HttpEntity<>(categoryDto);
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            // Handle the exception
            System.out.println("Error occurred while updating category: " + e.getMessage());
            log.error("Error occurred while updating category: {}", e.getMessage());
        }
    }

    public void deleteCategory(Long id) {
        try {
            String url = supplierUrl + "/api/categories/" + id;
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            // Handle the exception
            log.error("Error occurred while deleting category: {}", e.getMessage());
        }
    }
}