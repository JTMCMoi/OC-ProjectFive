package com.openclassrooms.mddapi.services.mappers;

import org.springframework.stereotype.Component;

import com.openclassrooms.mddapi.dto.topic.TopicCreateDto;
import com.openclassrooms.mddapi.dto.topic.TopicResponseDto;
import com.openclassrooms.mddapi.dto.topic.TopicUpdateDto;
import com.openclassrooms.mddapi.models.TopicEntity;

@Component
public class TopicMapper {

    public TopicResponseDto toDto(TopicEntity entity) {
        if (entity == null) return null;

        return new TopicResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public TopicEntity fromCreateDto(TopicCreateDto dto) {
        if (dto == null) return null;

        TopicEntity entity = new TopicEntity();
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        return entity;
    }

    public void updateEntityFromDto(TopicUpdateDto dto, TopicEntity entity) {
        if (dto == null || entity == null) return;

        if (dto.title() != null) {
            entity.setTitle(dto.title());
        }
        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }
    }
}
