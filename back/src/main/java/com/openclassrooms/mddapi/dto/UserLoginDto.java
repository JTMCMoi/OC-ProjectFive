package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDto(

        @NotBlank(message = "Identifiant ou email obligatoire")
        String usernameOrEmail,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password
) {}
