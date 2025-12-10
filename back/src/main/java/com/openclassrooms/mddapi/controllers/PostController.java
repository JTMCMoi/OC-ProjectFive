package com.openclassrooms.mddapi.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.dto.post.*;
import com.openclassrooms.mddapi.services.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @PostMapping
    public ResponseEntity<PostResponseDto> create(@RequestBody PostCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/topics/{topicId}")
    public ResponseEntity<List<PostResponseDto>> getByTopic(@PathVariable Long topicId) {
        return ResponseEntity.ok(service.getByTopic(topicId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}