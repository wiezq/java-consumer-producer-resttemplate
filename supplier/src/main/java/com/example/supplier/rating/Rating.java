package com.example.supplier.rating;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "average_rating", nullable = false)
    private Float averageRating = 0.0f;

    @Column(name = "total_rating", nullable = false)
    private Long totalRating = 0L;

    @Column(name = "total_reviews", nullable = false)
    private Integer totalReviews = 0;

    @Override
    public String toString() {
        return "Rating{" +
                "id" + getId() +
                "averageRating=" + averageRating +
                ", totalRating=" + totalRating +
                ", totalReviews=" + totalReviews +
                "} ";
    }
}