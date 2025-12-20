package com.openclassrooms.mddapi.dto.comment;

public record CommentListItemDto(
    Long id,
    String content,
    String authorUsername
) {}
