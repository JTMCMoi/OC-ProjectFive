package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UpdateUserRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "Utilisateur", description = "Profil de l'utilisateur connecté")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Mon profil", description = "Retourne les informations de l'utilisateur connecté")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profil retourné"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Operation(summary = "Mettre à jour mon profil", description = "Modifie le nom d'utilisateur et/ou l'email de l'utilisateur connecté")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profil mis à jour"),
        @ApiResponse(responseCode = "400", description = "Données invalides ou email/username déjà utilisé"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }
}
