package com.openclassrooms.mddapi.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateDto(

    @NotBlank
    @Size(max = 50)
    String title,

    @NotBlank
    String content
) {}
