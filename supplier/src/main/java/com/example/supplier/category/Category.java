package com.example.supplier.category;

import com.example.supplier.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "category")
@NoArgsConstructor
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "category",
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @Override
    public String toString() {
        return "Category{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                '}';
    }
}