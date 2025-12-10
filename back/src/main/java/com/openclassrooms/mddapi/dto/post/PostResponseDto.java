package com.openclassrooms.mddapi.dto.post;

import java.time.LocalDateTime;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        Long authorId,
        String authorUsername,
        Long topicId,
        String topicName,
        LocalDateTime createdAt
) {}
