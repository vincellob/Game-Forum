package com.team2.backend.DTO.UserReviewInteraction;

import com.team2.backend.Enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerInteractionDTO {
    private String username;
    private Long reviewid;
    private Integer appid;
    private String gameName;
    private NotificationType type;
}
