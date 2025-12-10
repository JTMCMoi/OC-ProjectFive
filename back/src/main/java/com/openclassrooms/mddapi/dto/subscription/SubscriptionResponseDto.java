package com.openclassrooms.mddapi.dto.subscription;

public record SubscriptionResponseDto(
    Long id,
    Long topicId,
    String topicName
) {}
