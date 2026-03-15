package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.ThemeResponse;
import com.openclassrooms.mddapi.model.Theme;
import org.springframework.stereotype.Component;

@Component
public class ThemeMapper {

    public ThemeResponse toResponse(Theme theme, boolean subscribed) {
        if (theme == null) {
            return null;
        }
        return ThemeResponse.builder()
                .id(theme.getId())
                .title(theme.getTitle())
                .description(theme.getDescription())
                .subscribed(subscribed)
                .createdAt(theme.getCreatedAt())
                .build();
    }
}

