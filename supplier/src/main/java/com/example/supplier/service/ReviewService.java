package com.example.supplier.service;

import com.example.supplier.exception.ResourceNotFoundException;
import com.example.supplier.model.Product;
import com.example.supplier.model.Review;
import com.example.supplier.repository.ProductRepository;
import com.example.supplier.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public Review createReview(Long productId, Review review) {
        Product product = productRepository.findByIdWithReviews(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        review.setProduct(product);
        Review savedReview = reviewRepository.save(review);
        recalculateAverageRating(product, savedReview);
        return savedReview;
    }

    private void recalculateAverageRating(Product product, Review review) {
        List<Review> reviews = product.getReviews();
        reviews.add(review);
        BigDecimal sum = BigDecimal.ZERO;
        int count = reviews.size();
        for (Review r : reviews) {
            sum = sum.add(BigDecimal.valueOf(r.getRating()));
        }
        BigDecimal averageRating = count > 0 ? sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        product.setAverageRating(averageRating);
        productRepository.save(product);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    public Review updateReview(Long id, Review review) {
        Review existingReview = getReviewById(id);
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());
        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        reviewRepository.delete(review);
    }
}