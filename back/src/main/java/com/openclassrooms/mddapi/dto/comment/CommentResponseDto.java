package com.openclassrooms.mddapi.dto.comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
    Long id,
    String content,
    Long authorId,
    String authorUsername,
    Long postId,
    LocalDateTime createdAt
) {}
