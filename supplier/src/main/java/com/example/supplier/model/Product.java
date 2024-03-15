package com.example.supplier.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    @Column(name = "name",
            columnDefinition = "VARCHAR(255)",
            nullable = false)
    private String name;

    @Column(name = "price",
            columnDefinition = "BIGINT",
            nullable = false)
    private Long price;

    @Column(name = "description",
            columnDefinition = "VARCHAR(1000)",
            nullable = false)
    private String description;

    @Column(name = "average_rating", nullable = false)
    private Float averageRating = 0.0f;

    @Column(name = "total_rating", nullable = false)
    private Long totalRating = 0L;

    @Column(name = "total_reviews", nullable = false)
    private Integer totalReviews = 0;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

}