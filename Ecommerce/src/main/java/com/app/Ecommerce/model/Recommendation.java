package com.app.Ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private int recommendationId;

    @ManyToOne
    @JoinColumn(name = "r_user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "r_product_id", nullable = false)
    private Product product;

    @Column(name = "similarity_score", nullable = false)
    private int similarityScore;
}