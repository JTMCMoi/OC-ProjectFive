package com.openclassrooms.mddapi.services.mappers;


import org.mapstruct.*;

import com.openclassrooms.mddapi.dto.UserRegisterDto;
import com.openclassrooms.mddapi.dto.UserResponseDto;
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
}
