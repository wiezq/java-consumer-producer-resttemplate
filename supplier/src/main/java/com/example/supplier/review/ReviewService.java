package com.example.supplier.review;

import com.example.supplier.exception.ResourceNotFoundException;
import com.example.supplier.product.Product;
import com.example.supplier.product.ProductService;
import com.example.supplier.rating.Rating;
import com.example.supplier.rating.RatingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final ProductService productService;

    private final RatingService ratingService;


    // Create a review and recalculate the average rating of the product
    public Review createReview(Long productId, Review review) {
        // Find product
        Product product = productService.getProductById(productId);

        // Save Review
        review.setProduct(product);
        Review newReview = reviewRepository.save(review);

        Rating rating = product.getRating();

        // Update rating with new review
        ratingService.createReviewCase(rating, newReview);

        return newReview;
    }

    public List<Review> getAllReviews(Long id) {
        return reviewRepository.findAllWithProductId(id);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, "Review not found"));
    }

    public Review updateReview(Long id, Review updatedReview) {
        Review oldReview = getReviewById(id);

        Rating rating = oldReview.getProduct().getRating();

        ratingService.updateReviewCase(rating, oldReview, updatedReview);

        // Update review
        oldReview.setRating(updatedReview.getRating());
        oldReview.setComment(updatedReview.getComment());
        return reviewRepository.save(oldReview);
    }

    public void deleteReview(Long id) {
        Review review = getReviewById(id);

        Rating rating = review.getProduct().getRating();

        ratingService.deleteReviewCase(rating, review);

        reviewRepository.delete(review);
    }
}