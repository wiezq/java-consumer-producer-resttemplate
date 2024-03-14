package com.example.supplier.repository;

import com.example.supplier.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.reviews left join fetch p.reviews WHERE p.id = :id")
    Optional<Product> findByIdWithReviews(Long id);
}