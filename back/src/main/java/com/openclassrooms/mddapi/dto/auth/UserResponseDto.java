package com.openclassrooms.mddapi.dto.auth;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record UserResponseDto(
        Long id,
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}