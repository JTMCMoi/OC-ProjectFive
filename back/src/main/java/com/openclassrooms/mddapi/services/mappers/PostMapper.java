package com.openclassrooms.mddapi.services.mappers;

import org.springframework.stereotype.Component;

import com.openclassrooms.mddapi.dto.post.*;
import com.openclassrooms.mddapi.models.PostEntity;

@Component
public class PostMapper {

    public PostEntity fromCreateDto(PostCreateDto dto) {
        PostEntity e = new PostEntity();
        e.setTitle(dto.title());
        e.setContent(dto.content());
        return e;
    }

    public PostResponseDto toDto(PostEntity e) {
        return new PostResponseDto(
                e.getId(),
                e.getTitle(),
                e.getContent(),
                e.getAuthor().getId(),
                e.getAuthor().getUsername(),
                e.getTopic().getId(),
                e.getTopic().getTitle(),
                e.getCreatedAt()
        );
    }
}
