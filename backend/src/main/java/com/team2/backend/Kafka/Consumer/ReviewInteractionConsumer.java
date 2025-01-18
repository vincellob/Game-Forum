package com.team2.backend.Kafka.Consumer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.backend.Repository.NotificationRepository;
import com.team2.backend.Repository.ReviewRepository;
import com.team2.backend.DTO.UserReviewInteraction.ProducerInteractionDTO;
import com.team2.backend.Enums.NotificationType;
import com.team2.backend.Models.Notification;
import com.team2.backend.Models.Review;

@Service
public class ReviewInteractionConsumer {

    @Autowired
    private NotificationRepository notificationRepository;

    // @Autowired
    // private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topic.review-interaction}", groupId = "game-forum-group")
    public void consumeReviewInteraction(String message) {
        try {
            ProducerInteractionDTO interactionDTO = objectMapper.readValue(message, ProducerInteractionDTO.class);
            handleReviewInteraction(interactionDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleReviewInteraction(ProducerInteractionDTO interactionDTO) {
        System.out.println("Processing review interaction: " + interactionDTO);

        // Example logic
        if (interactionDTO.getType() == NotificationType.LIKE) {
            System.out.println(
                    "Review " + interactionDTO.getReviewid() + " liked by user " + interactionDTO.getReviewid());
            sendNotification(interactionDTO, "Your review was liked!");
        } else if (interactionDTO.getType() == NotificationType.DISLIKE) {
            System.out.println(
                    "Review " + interactionDTO.getReviewid() + " disliked by user " + interactionDTO.getReviewid());
            sendNotification(interactionDTO, "Your review was disliked!");

        }
        // Further processing (e.g., updating analytics or notifying users)
    }

    private void sendNotification(ProducerInteractionDTO interactionDTO, String message) {
        Notification notification = new Notification();
        Review review = reviewRepository.findById(interactionDTO.getReviewid()).get();
        // Optional<User> user = userRepository.findById(review.getUser().getId());

        // System.out.println("****APID: " + review.getAppid());

        // Define a set of equivalent types
        Set<NotificationType> equivalentTypes = new HashSet<>();
        equivalentTypes.add(NotificationType.LIKE);
        equivalentTypes.add(NotificationType.DISLIKE);
        
        // Check for existing notification considering equivalent types
        Optional<Notification> existingNotification = notificationRepository.findByUserAndReviewIdAndTypeIn(
                review.getUser(), interactionDTO.getReviewid(), equivalentTypes);
        
        
        if (existingNotification.isPresent()) {
            // Notification already exists, update it with the new type
            notification = existingNotification.get();
            notification.setType(interactionDTO.getType());
            notificationRepository.save(notification);
            System.out.println("Notification updated due to like/dislike switch.");
        } else {
            // Create a new notification
            notification = new Notification();
            notification.setUser(review.getUser());
            notification.setGameName(interactionDTO.getGameName());
            notification.setReviewId(interactionDTO.getReviewid());
            notification.setAppId(review.getAppid());
            notification.setUsername(interactionDTO.getUsername());
            notification.setType(interactionDTO.getType());
            notificationRepository.save(notification);
            System.out.println("New notification created.");
        }


    }

}
