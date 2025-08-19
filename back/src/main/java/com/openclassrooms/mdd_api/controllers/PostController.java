package com.openclassrooms.mdd_api.controllers;

import com.openclassrooms.mdd_api.dto.post.CreatePostRequestDto;
import com.openclassrooms.mdd_api.exception.ApiException;
import com.openclassrooms.mdd_api.service.implementations.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
public class PostController {
    private final PostService postService;

    PostController(final PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public ResponseEntity<?> getPosts(final JwtAuthenticationToken token) {
        try {
            return ResponseEntity.ok().body(this.postService.getAllPostsByTopicIds(token));
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable final int id) {
        try {
            return ResponseEntity.ok().body(this.postService.getPostById(id));
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(final JwtAuthenticationToken token, @Valid @RequestBody final CreatePostRequestDto request) {
        try {
            this.postService.createPost(token, request);
            return ResponseEntity.ok().build();
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
