package com.team2.backend.Kafka.Producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.backend.DTO.UserReviewInteraction.ProducerInteractionDTO;

@Service
public class ReviewInteractionProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.review-interaction}")
    private String reviewInteractionTopic;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendReviewInteraction(ProducerInteractionDTO interactionDTO) {
        try {
            String message = objectMapper.writeValueAsString(interactionDTO);
            kafkaTemplate.send(reviewInteractionTopic, message);
            System.out.println("Sent review interaction to Kafka: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
