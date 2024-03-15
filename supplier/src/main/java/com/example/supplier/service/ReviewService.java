package com.example.supplier.service;

import com.example.supplier.exception.ResourceNotFoundException;
import com.example.supplier.model.Product;
import com.example.supplier.model.Review;
import com.example.supplier.repository.ProductRepository;
import com.example.supplier.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }


    // Create a review and recalculate the average rating of the product
    public Review createReview(Long productId, Review review) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId, "Product not found"));
        review.setProduct(product);
        Review savedReview = reviewRepository.save(review);

        // Update the total rating and total reviews of the product with bigdecimal
        int newTotalReviews = product.getTotalReviews() + 1;
        long newTotalRating = product.getTotalRating() + review.getRating();
        float newAverageRating = BigDecimal
                .valueOf(newTotalRating)
                .divide(BigDecimal.valueOf(newTotalReviews), 2, RoundingMode.HALF_UP)
                .floatValue();
        product.setTotalRating(newTotalRating);
        product.setTotalReviews(newTotalReviews);
        product.setAverageRating(newAverageRating);
        productRepository.save(product);

        return savedReview;
    }

    public List<Review> getAllReviews(Long id) {
        return reviewRepository.findAllWithProductId(id);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, "Review not found"));
    }

    public Review updateReview(Long id, Review review) {
        Review existingReview = getReviewById(id);


        // Update the total rating and total reviews of the product with bigdecimal
        Product product = existingReview.getProduct();
        long newTotalRating = product.getTotalRating() - existingReview.getRating() + review.getRating();
        float newAverageRating = BigDecimal
                .valueOf(newTotalRating)
                .divide(BigDecimal.valueOf(product.getTotalReviews()), 2, RoundingMode.HALF_UP)
                .floatValue();
        product.setTotalRating(newTotalRating);
        product.setAverageRating(newAverageRating);
        productRepository.save(product);

        // Update review
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());
        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long id) {
        Review review = getReviewById(id);


        // Update the total rating and total reviews of the product with bigdecimal
        Product product = review.getProduct();
        long newTotalRating = product.getTotalRating() - review.getRating();
        float newAverageRating = BigDecimal
                .valueOf(newTotalRating)
                .divide(BigDecimal.valueOf(product.getTotalReviews() - 1), 2, RoundingMode.HALF_UP)
                .floatValue();
        product.setTotalRating(newTotalRating);
        product.setTotalReviews(product.getTotalReviews() - 1);
        product.setAverageRating(newAverageRating);

        reviewRepository.delete(review);
    }
}