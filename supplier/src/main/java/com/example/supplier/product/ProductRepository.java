package com.example.supplier.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph(attributePaths = {"category", "rating"})
    Optional<Product> findById(Long id);

    @EntityGraph(attributePaths = {"category", "rating"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}