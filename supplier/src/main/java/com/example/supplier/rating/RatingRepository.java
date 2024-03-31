package com.example.supplier.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}