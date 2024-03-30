package com.example.supplier.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @Operation(
            summary = "Create review",
            description = "Create review and recalculate product rating",
            tags = {"reviews"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created review",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request, validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))

            }
    )
    @PostMapping("/products/{productId}")
    public ResponseEntity<ReviewDto> createReview(@PathVariable @Min(1) Long productId,
                                                  @RequestBody ReviewDto reviewDto) {
        Review review = mapToEntity(reviewDto);
        Review createdReview = reviewService.createReview(productId, review);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(createdReview));
    }


    @Operation(
            summary = "Get all reviews",
            description = "Get all reviews",
            tags = {"reviews"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of reviews",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewDto.class)))
            }
    )
    @GetMapping(value = "/products/{id}", produces = "application/json")
    public ResponseEntity<List<ReviewDto>> getAllReviews(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                reviewService.getAllReviews(id).stream()
                        .map(this::mapToDto)
                        .toList());
    }


    @Operation(
            summary = "Get review by id",
            description = "Get review by id",
            tags = {"reviews"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review by id",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok().body(mapToDto(reviewService.getReviewById(id)));
    }


    @Operation(
            summary = "Update review",
            description = "Update review, and recalculate product rating",
            tags = {"reviews"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated review",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable @Min(1) Long id,
                                                  @RequestBody ReviewDto reviewDto) {
        Review review = mapToEntity(reviewDto);
        Review updatedReview = reviewService.updateReview(id, review);
        return ResponseEntity.ok().body(mapToDto(updatedReview));
    }


    @Operation(
            summary = "Delete review",
            description = "Delete review by id",
            tags = {"reviews"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable @Min(1) Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
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