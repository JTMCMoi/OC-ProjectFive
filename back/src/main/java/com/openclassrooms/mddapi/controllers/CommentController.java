package com.openclassrooms.mddapi.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.dto.comment.*;
import com.openclassrooms.mddapi.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @PostMapping
    public ResponseEntity<CommentResponseDto> create(@RequestBody CommentCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentListItemDto>> getByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(service.getByPost(postId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
