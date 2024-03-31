package com.example.consumer.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ConsumerReviewService {

    private final RestTemplate restTemplate;

    @Value("${api.url}")
    private String supplierUrl;

    public ConsumerReviewService() {
        this.restTemplate = new RestTemplate();
    }

    public ReviewDto createReview(Long productId, ReviewDto reviewDto) {
        try {
            String url = supplierUrl + "/api/reviews/products/" + productId;
            ResponseEntity<ReviewDto> response = restTemplate.postForEntity(url, reviewDto, ReviewDto.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while creating review: {}", e.getMessage());
            return null;
        }
    }

    public ReviewDto getReviewById(Long id) {
        try {
            String url = supplierUrl + "/api/reviews/" + id;
            ResponseEntity<ReviewDto> response = restTemplate.getForEntity(url, ReviewDto.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while fetching review by id: {}", e.getMessage());
            return null;
        }
    }

    public List<ReviewDto> getAllReviewsByProductId(Long categoryId) {
        try {
            String url = supplierUrl + "/api/reviews/products/" + categoryId;
            ResponseEntity<List<ReviewDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ReviewDto>>() {});
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while fetching reviews by category id: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void updateReview(Long id, ReviewDto reviewDto) {
        try {
            String url = supplierUrl + "/api/reviews/" + id;
            restTemplate.put(url, reviewDto);
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while updating review: {}", e.getMessage());
        }
    }

    public void deleteReview(Long id) {
        try {
            String url = supplierUrl + "/api/reviews/" + id;
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("Error occurred while deleting review: {}", e.getMessage());
        }
    }
}