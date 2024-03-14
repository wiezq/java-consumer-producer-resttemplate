package com.example.supplier.controller;

import com.example.supplier.dto.ReviewDto;
import com.example.supplier.model.Review;
import com.example.supplier.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/products/{productId}")
    public ReviewDto createReview(@PathVariable Long productId, @RequestBody ReviewDto reviewDto) {
        Review review = mapToEntity(reviewDto);
        Review createdReview = reviewService.createReview(productId, review);
        return mapToDto(createdReview);
    }

    @GetMapping
    public List<ReviewDto> getAllReviews() {
        return reviewService.getAllReviews().stream()
                .map(this::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ReviewDto getReviewById(@PathVariable Long id) {
        return mapToDto(reviewService.getReviewById(id));
    }


    @PutMapping("/{id}")
    public ReviewDto updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        Review review = mapToEntity(reviewDto);
        Review updatedReview = reviewService.updateReview(id, review);
        return mapToDto(updatedReview);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    private Review mapToEntity(ReviewDto reviewDto) {
        Review review = new Review();
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        return review;
    }

    private ReviewDto mapToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setRating(review.getRating());
        reviewDto.setComment(review.getComment());
        reviewDto.setProductId(review.getProduct().getId());
        return reviewDto;
    }
}