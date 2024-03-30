package com.example.supplier.rating;

import com.example.supplier.review.Review;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public void updateRating(Rating rating,
                             int totalReviews,
                             long totalRating,
                             float averageRating){
        rating.setTotalReviews(totalReviews);
        rating.setTotalRating(totalRating);
        rating.setAverageRating(averageRating);
        ratingRepository.save(rating);
    }

    public void createReviewCase(Rating rating, Review newReview) {
        int newTotalReviews = rating.getTotalReviews() + 1;
        long newTotalRating = rating.getTotalRating() + newReview.getRating();
        float newAverageRating = calculateAverageRating(newTotalReviews, newTotalRating);
        updateRating(rating, newTotalReviews, newTotalRating, newAverageRating);
    }

    public void updateReviewCase(Rating rating, Review oldReview, Review updatedReview) {
        int newTotalReviews = rating.getTotalReviews();
        long newTotalRating = rating.getTotalRating() - oldReview.getRating() + updatedReview.getRating();
        float newAverageRating = calculateAverageRating(newTotalReviews, newTotalRating);
        updateRating(rating, newTotalReviews, newTotalRating, newAverageRating);

    }

    public void deleteReviewCase(Rating rating, Review deletedReview) {
        int newTotalReviews = rating.getTotalReviews() - 1;
        long newTotalRating = rating.getTotalRating() - deletedReview.getRating();
        float newAverageRating = calculateAverageRating(newTotalReviews, newTotalRating);
        updateRating(rating, newTotalReviews, newTotalRating, newAverageRating);
    }

    private float calculateAverageRating(int totalReviews, long totalRating) {
        return BigDecimal
                .valueOf(totalRating)
                .divide(BigDecimal.valueOf(totalReviews), 2, RoundingMode.HALF_UP)
                .floatValue();
    }
}
