package com.team2.backend.Kafka.Consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.backend.Repository.GameRepository;
import com.team2.backend.Repository.NotificationRepository;
import com.team2.backend.Repository.ReviewRepository;
import com.team2.backend.DTO.UserReviewInteraction.ProducerInteractionDTO;
import com.team2.backend.Enums.NotificationType;
import com.team2.backend.Models.Game;
import com.team2.backend.Models.Notification;
import com.team2.backend.Models.Review;
import com.team2.backend.Models.User;

@Service
public class ReviewCreationConsumer {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topic.review-creation}", groupId = "game-forum-group")
    public void consumeReviewInteraction(String message) {
        try {
            ProducerInteractionDTO interactionDTO = objectMapper.readValue(message, ProducerInteractionDTO.class);
            handleNewReview(interactionDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNewReview(ProducerInteractionDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewDTO.getReviewid()).get();
        List<Game> games = gameRepository.findByAppId(review.getAppid());
        for (Game game : games) {
            // Skip sending notification to the user who created the review
            if (!game.getUser().getId().equals(review.getUser().getId())) {
                sendNotification(game.getUser(), review, game);
            }
        }
    }

    private void sendNotification(User user, Review review, Game game) {
        System.out.println("Sending review creation notification to user: " + user.getUsername());
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setGameName(game.getName());
        notification.setReviewId(review.getId());
        notification.setAppId(review.getAppid());
        notification.setUsername(review.getUser().getUsername());
        notification.setType(NotificationType.REVIEW);

        notificationRepository.save(notification);

    }

}
