package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.UserResponseDto;
import com.openclassrooms.mddapi.services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    /**
     * GET /api/users/{id}
     * Retourne les infos publiques d’un utilisateur
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {

        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/users/{id}
     * Suppression par l’utilisateur lui-même.
     * Ne fonctionne QUE si l’utilisateur connecté correspond.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        var optional = userService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserResponseDto userDto = optional.get();

        // Récupération du user connecté via le JWT
        UserDetails connectedUser =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        // Vérification : seul le propriétaire peut supprimer son compte
        if (!connectedUser.getUsername().equals(userDto.email())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/users/me
     * Renvoie l'utilisateur courant (via le JWT)
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {

        UserDetails connectedUser =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = connectedUser.getUsername();

        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
