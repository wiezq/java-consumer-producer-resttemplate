package com.example.consumer.service;

import com.example.consumer.dto.ProductFilterRequest;
import com.example.consumer.dto.ProductDto;
import com.example.consumer.dto.ProductResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class ConsumerProductService {

    private final RestTemplate restTemplate;


    @Value("${api.url}")
    private String supplierApiUrl;


    public ConsumerProductService(){
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<ProductResponseDto> getAllProducts(int page, int size, String sort, String sortBy, ProductFilterRequest filterRequest) {

        String url = supplierApiUrl + "/api/products";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sort)
                .queryParam("sortBy", sortBy)
                .queryParam("categoryId", filterRequest.getCategoryId())
                .queryParam("minPrice", filterRequest.getMinPrice())
                .queryParam("maxPrice", filterRequest.getMaxPrice())
                .queryParam("minRating", filterRequest.getMinRating())
                .queryParam("maxRating", filterRequest.getMaxRating())
                .queryParam("description", filterRequest.getDescription());


        // Perform the request
        return restTemplate.exchange(uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ProductResponseDto>() {
                });
    }

    public ResponseEntity<ProductDto> getProductById(Long id) {
        String url = supplierApiUrl + "/api/products/" + id;
        return restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ProductDto>() {
                });
    }

    public ResponseEntity<ProductDto> createProduct(ProductDto productDto) {
        String url = supplierApiUrl + "/api/products";
        return restTemplate.postForEntity(url, productDto, ProductDto.class);
    }

    public ResponseEntity<ProductDto> updateProduct(Long id, ProductDto productDto) {
        String url = supplierApiUrl + "/api/products/" + id;
        return restTemplate.exchange(url,
                HttpMethod.PUT,
                new HttpEntity<>(productDto),
                new ParameterizedTypeReference<ProductDto>() {
                });
    }

    public void deleteProduct(Long id) {
        String url = supplierApiUrl + "/api/products/" + id;
        restTemplate.delete(url);
    }
}