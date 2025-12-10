package com.openclassrooms.mddapi.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TopicUpdateDto(
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 50, message = "Le titre ne peut dépasser 50 caractères")
    String title,

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 255, message = "La description ne peut dépasser 255 caractères")
    String description
) {}
