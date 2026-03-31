package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.dto.PostRequest;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Articles", description = "Fil d'actualité, détail et création d'articles, commentaires")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    private final PostService postService;

    @Operation(summary = "Fil d'actualité", description = "Retourne les articles des thèmes auxquels l'utilisateur est abonné")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des articles"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getFeed() {
        return ResponseEntity.ok(postService.getFeed());
    }

    @Operation(summary = "Détail d'un article", description = "Retourne un article avec ses commentaires")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article trouvé"),
        @ApiResponse(responseCode = "404", description = "Article introuvable"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @Operation(summary = "Créer un article", description = "Publie un nouvel article dans un thème auquel l'utilisateur est abonné")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article créé"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "403", description = "Non abonné au thème"),
        @ApiResponse(responseCode = "404", description = "Thème introuvable"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @Operation(summary = "Ajouter un commentaire", description = "Ajoute un commentaire à un article existant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Commentaire ajouté"),
        @ApiResponse(responseCode = "404", description = "Article introuvable"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long id, @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(postService.addComment(id, request));
    }
}
