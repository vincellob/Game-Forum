package com.team2.backend.DTO.UserReviewInteraction;

import com.team2.backend.Models.Review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInteractionResultDTO {
  private Integer likes;
  private Integer dislikes;

  public UserInteractionResultDTO(Review review) {
    this.likes = review.getLikes();
    this.dislikes = review.getDislikes();
  }
}
