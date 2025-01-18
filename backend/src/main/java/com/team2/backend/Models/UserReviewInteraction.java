package com.team2.backend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.team2.backend.DTO.UserReviewInteraction.UserReviewInteractionDTO;
import com.team2.backend.Enums.ReviewInteraction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "user_review_interaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserReviewInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewInteraction interaction;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "review_id", nullable = false)
    @ToString.Exclude
    private Review review;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    public UserReviewInteraction(UserReviewInteractionDTO userreview, User user, Review review) {
        this.user = user;
        this.review = review;
        this.interaction = userreview.getInteraction();
    }
}
