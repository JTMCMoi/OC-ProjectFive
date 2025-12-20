package com.openclassrooms.mddapi.services.mappers;

import org.springframework.stereotype.Component;

import com.openclassrooms.mddapi.dto.comment.*;
import com.openclassrooms.mddapi.models.CommentEntity;

@Component
public class CommentMapper {

    public CommentEntity fromCreateDto(CommentCreateDto dto) {
        CommentEntity e = new CommentEntity();
        e.setContent(dto.content());
        return e;
    }

    public CommentResponseDto toDto(CommentEntity e) {
        return new CommentResponseDto(
            e.getId(),
            e.getContent(),
            e.getAuthor().getId(),
            e.getAuthor().getUsername(),
            e.getPost().getId(),
            e.getCreatedAt()
        );
    }

    public CommentListItemDto toListItem(CommentEntity e) {
        return new CommentListItemDto(
            e.getId(),
            e.getContent(),
            e.getAuthor().getUsername()
        );
    }
}
