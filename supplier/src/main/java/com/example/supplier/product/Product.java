package com.example.supplier.product;

import com.example.supplier.category.Category;
import com.example.supplier.rating.Rating;
import com.example.supplier.review.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @JoinColumn(name = "rating_id")
    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Rating rating;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                "name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", rating=" + rating +
                '}';
    }
}