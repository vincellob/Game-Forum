package com.team2.backend.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.backend.Enums.NotificationType;
import com.team2.backend.Models.Notification;
import com.team2.backend.Models.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long>{
    List<Notification> findByUser(User user);

    List<Notification> findByUserAndType(User user, NotificationType type);

    Optional<Notification> findByUserAndReviewIdAndType(User user, Long reviewId, NotificationType type);

    Optional<Notification> findByUserAndReviewIdAndTypeIn(User user, Long reviewid,
            Set<NotificationType> equivalentTypes);
    
}
