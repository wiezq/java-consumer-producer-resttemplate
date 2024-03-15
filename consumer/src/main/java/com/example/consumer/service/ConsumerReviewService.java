package com.example.consumer.service;

import com.example.consumer.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumerReviewService {


    private final RestTemplate restTemplate;

    @Value("${api.url}")
    String url;

    public ConsumerReviewService() {
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<ReviewDto> createReview(Long productId, ReviewDto reviewDto) {
        return restTemplate.postForEntity(url + "/api/reviews/products/" + productId, reviewDto, ReviewDto.class);
    }

    public ResponseEntity<ReviewDto> getReview(Long id) {
        return restTemplate.exchange(
                url + "/api/reviews/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
    }

    public ResponseEntity<ReviewDto> updateReview(Long id, ReviewDto reviewDto) {
        return restTemplate.exchange(
                url + id,
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                });
    }

    public void deleteReview(Long id) {
        restTemplate.delete(url + "/api/reviews/" + id);
    }

    public ResponseEntity<String> getAllReviewsByProductId(Long id) {

        return restTemplate.exchange(
                url + "/api/reviews/products/" + id,
                HttpMethod.GET,
                null,
                String.class);
    }

}
