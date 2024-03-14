package com.example.supplier.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            columnDefinition = "DECIMAL(19,2)",
            precision = 19,
            scale = 2,
            nullable = false)
    private BigDecimal price;

    @Column(name = "description",
            columnDefinition = "TEXT",
            nullable = false)
    private String description;

    @Column(name = "average_rating", columnDefinition = "DECIMAL(3,2)", precision = 3, scale = 2)
    private BigDecimal averageRating;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

}