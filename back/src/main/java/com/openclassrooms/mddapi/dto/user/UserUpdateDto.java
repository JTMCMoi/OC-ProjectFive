package com.openclassrooms.mddapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
    @Size(max = 50, message = "Le nom d'utilisateur ne peut dépasser 50 caractères")
    String username,

    @Email(message = "L'adresse email n'est pas valide")
    @Size(max = 255)
    String email,

    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial"
    )
    String password
    ) {}






