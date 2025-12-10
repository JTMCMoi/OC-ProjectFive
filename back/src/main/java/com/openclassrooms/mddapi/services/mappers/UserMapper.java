package com.openclassrooms.mddapi.services.mappers;


import org.mapstruct.*;

import com.openclassrooms.mddapi.dto.auth.UserRegisterDto;
import com.openclassrooms.mddapi.dto.auth.UserResponseDto;
import com.openclassrooms.mddapi.dto.user.UserUpdateDto;
import com.openclassrooms.mddapi.models.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Map entity → DTO
    UserResponseDto toDto(UserEntity user);

    // Map DTO → entity (register)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity fromRegisterDto(UserRegisterDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true) // géré dans le service
    void updateEntityFromDto(UserUpdateDto dto, @MappingTarget UserEntity entity);
}
