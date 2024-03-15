package com.example.supplier.repository;

import com.example.supplier.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.product where r.product.id = :productId")
    List<Review> findAllWithProductId(@Param("productId") Long productId);
}