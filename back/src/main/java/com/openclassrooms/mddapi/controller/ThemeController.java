package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.MessageResponse;
import com.openclassrooms.mddapi.dto.ThemeResponse;
import com.openclassrooms.mddapi.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Thèmes", description = "Consultation et gestion des abonnements aux thèmes")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ThemeController {

    private final ThemeService themeService;

    /**
     * GET /api/themes
     * Retourne tous les thèmes avec le flag "subscribed" selon l'utilisateur connecté.
     */
    @Operation(summary = "Tous les thèmes", description = "Retourne tous les thèmes avec le flag 'subscribed' de l'utilisateur connecté")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des thèmes"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemes());
    }

    /**
     * GET /api/themes/subscribed
     * Retourne uniquement les thèmes auxquels l'utilisateur est abonné.
     */
    @Operation(summary = "Thèmes abonnés", description = "Retourne uniquement les thèmes auxquels l'utilisateur est abonné")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des thèmes abonnés"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/subscribed")
    public ResponseEntity<List<ThemeResponse>> getSubscribedThemes() {
        return ResponseEntity.ok(themeService.getSubscribedThemes());
    }

    /**
     * POST /api/themes/{id}/subscribe
     * Abonne l'utilisateur connecté au thème.
     */
    @Operation(summary = "S'abonner à un thème", description = "Abonne l'utilisateur connecté au thème spécifié")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Abonnement effectué"),
        @ApiResponse(responseCode = "404", description = "Thème introuvable"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<MessageResponse> subscribe(@PathVariable Long id) {
        themeService.subscribe(id);
        return ResponseEntity.ok(new MessageResponse("Abonnement au thème effectué avec succès"));
    }

    /**
     * DELETE /api/themes/{id}/subscribe
     * Désabonne l'utilisateur connecté du thème.
     */
    @Operation(summary = "Se désabonner d'un thème", description = "Désabonne l'utilisateur connecté du thème spécifié")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Désabonnement effectué"),
        @ApiResponse(responseCode = "404", description = "Thème introuvable"),
        @ApiResponse(responseCode = "403", description = "Non abonné à ce thème"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @DeleteMapping("/{id}/subscribe")
    public ResponseEntity<MessageResponse> unsubscribe(@PathVariable Long id) {
        themeService.unsubscribe(id);
        return ResponseEntity.ok(new MessageResponse("Désabonnement du thème effectué avec succès"));
    }
}
