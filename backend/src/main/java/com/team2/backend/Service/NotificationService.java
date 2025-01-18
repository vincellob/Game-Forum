package com.team2.backend.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team2.backend.Enums.NotificationType;
import com.team2.backend.Exceptions.InvalidCredentialsException;
import com.team2.backend.Models.Notification;
import com.team2.backend.Models.User;
import com.team2.backend.Repository.NotificationRepository;
import com.team2.backend.Repository.UserRepository;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Notification> getUnseenNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));
        return notificationRepository.findByUser(user);
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public void deleteAllNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));
        List<Notification> notifications = notificationRepository.findByUser(user);
        notificationRepository.deleteAll(notifications);
    }

    public List<Notification> getNotificationsByType(String username, NotificationType type) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));
        return notificationRepository.findByUserAndType(user, type);
    }

}
