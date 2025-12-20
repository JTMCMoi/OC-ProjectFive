package com.openclassrooms.mddapi.services.mappers;

import org.springframework.stereotype.Component;

import com.openclassrooms.mddapi.dto.auth.UserRegisterDto;
import com.openclassrooms.mddapi.dto.auth.UserResponseDto;
import com.openclassrooms.mddapi.dto.user.UserUpdateDto;
import com.openclassrooms.mddapi.models.UserEntity;

@Component
public class UserMapper {

    public UserResponseDto toDto(UserEntity user) {
        if (user == null) return null;

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public UserEntity fromRegisterDto(UserRegisterDto dto) {
        if (dto == null) return null;

        UserEntity entity = new UserEntity();
        entity.setUsername(dto.username());
        entity.setEmail(dto.email());
        // password géré dans le service
        return entity;
    }

    public void updateEntityFromDto(UserUpdateDto dto, UserEntity entity) {
        if (dto == null || entity == null) return;

        if (dto.username() != null) {
            entity.setUsername(dto.username());
        }
        if (dto.email() != null) {
            entity.setEmail(dto.email());
        }
        // password volontairement ignoré
    }
}
