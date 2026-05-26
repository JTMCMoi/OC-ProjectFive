package com.openclassrooms.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.Models.SubscriptionEntity;
import com.openclassrooms.Models.SubscriptionId;
import com.openclassrooms.Models.UserEntity;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, SubscriptionId> {

    List<SubscriptionEntity> findByUser(UserEntity userId);
    boolean existsById(SubscriptionId id);
}
