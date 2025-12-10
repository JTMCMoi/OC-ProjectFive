package com.openclassrooms.mddapi.dto.topic;

import java.time.LocalDateTime;

public record TopicResponseDto(
    Long id,
    String title,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
