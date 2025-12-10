package com.openclassrooms.mddapi.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.openclassrooms.mddapi.dto.topic.TopicCreateDto;
import com.openclassrooms.mddapi.dto.topic.TopicResponseDto;
import com.openclassrooms.mddapi.dto.topic.TopicUpdateDto;
import com.openclassrooms.mddapi.models.TopicEntity;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    // Map entity -> dto
    TopicResponseDto toDto(TopicEntity entity);

    // Map create dto -> entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TopicEntity fromCreateDto(TopicCreateDto dto);

    // Map update dto -> entity (used for copying values)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TopicEntity fromUpdateDto(TopicUpdateDto dto);
}
