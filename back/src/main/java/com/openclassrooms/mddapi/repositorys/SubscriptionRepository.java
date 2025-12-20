package com.openclassrooms.mddapi.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassrooms.mddapi.models.SubscriptionEntity;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> findByUserId(Long userId);

    Optional<SubscriptionEntity> findByUserIdAndTopicId(Long userId, Long topicId);

    boolean existsByUserIdAndTopicId(Long userId, Long topicId);
}
