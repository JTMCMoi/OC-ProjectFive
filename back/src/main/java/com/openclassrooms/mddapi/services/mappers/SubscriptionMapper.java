package com.openclassrooms.mddapi.services.mappers;

import org.springframework.stereotype.Component;

import com.openclassrooms.mddapi.dto.subscription.SubscriptionResponseDto;
import com.openclassrooms.mddapi.models.SubscriptionEntity;

@Component
public class SubscriptionMapper {

    public SubscriptionResponseDto toDto(SubscriptionEntity e) {
        return new SubscriptionResponseDto(
            e.getId(),
            e.getTopic().getId(),
            e.getTopic().getTitle()
        );
    }
}
