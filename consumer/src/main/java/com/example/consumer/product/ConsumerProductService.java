package com.example.consumer.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@Slf4j
public class ConsumerProductService {

    private final RestTemplate restTemplate;


    @Value("${api.url}")
    private String supplierApiUrl;


    public ConsumerProductService() {
        this.restTemplate = new RestTemplate();
    }

    public ProductResponseDto getProductById(Long id) {
        try {
            String url = supplierApiUrl + "/api/products/" + id;
            ResponseEntity<ProductResponseDto> response = restTemplate.getForEntity(url, ProductResponseDto.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while fetching product by id: {}", e.getMessage());
            return null;
        }
    }

    public ProductResponseDto createProduct(ProductCreateDto productDto) {
        try {
            String url = supplierApiUrl + "/api/products";
            ResponseEntity<ProductResponseDto> response = restTemplate.postForEntity(url, productDto, ProductResponseDto.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while creating product: {}", e.getMessage());
            return null;
        }
    }

    public void updateProduct(ProductUpdateDto productDto) {
        try {
            String url = supplierApiUrl + "/api/products/" + productDto.getId();
            HttpEntity<ProductUpdateDto> requestEntity = new HttpEntity<>(productDto);
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while updating product: {}", e.getMessage());
        }
    }

    public void deleteProduct(Long id) {
        try {
            String url = supplierApiUrl + "/api/products/" + id;
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while deleting product: {}", e.getMessage());
        }
    }

    public PageResponse<ProductResponseDto> getAllProductsWithFilter(
            @NotNull @Min(0) Integer page,
            @NotNull @Min(1) Integer size,
            @NotNull String sortDir,
            @NotNull String sortBy,
            Map<String, String> filterParams) {
        // Validate filter parameters
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be less than 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size cannot be less than 1");
        }
        if (!sortDir.equalsIgnoreCase("ASC") && !sortDir.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("Sort direction must be 'ASC' or 'DESC'");
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(supplierApiUrl)
                .path("/api/products/filter")
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortDir", sortDir)
                .queryParam("sortBy", sortBy);
        if (filterParams != null) filterParams.forEach(builder::queryParam);

        try {
            ResponseEntity<PageResponse<ProductResponseDto>> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PageResponse<ProductResponseDto>>() {});
            return response.getBody();
        } catch (HttpClientErrorException.BadRequest e) {
            log.error("Invalid filter parameters: {}", e.getMessage());
        } catch (RestClientException e) {
            log.error("Error occurred while fetching products: {}", e.getMessage());
        }
        return null;
    }
}