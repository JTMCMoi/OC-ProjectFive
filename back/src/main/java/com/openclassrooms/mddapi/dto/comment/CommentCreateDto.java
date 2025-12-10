package com.openclassrooms.mddapi.dto.comment;

public record CommentCreateDto(
    String content,
    Long postId
) {}
