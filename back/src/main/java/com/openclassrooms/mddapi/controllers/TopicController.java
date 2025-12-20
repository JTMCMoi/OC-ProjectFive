package com.openclassrooms.mddapi.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.dto.topic.TopicCreateDto;
import com.openclassrooms.mddapi.dto.topic.TopicResponseDto;
import com.openclassrooms.mddapi.dto.topic.TopicUpdateDto;
import com.openclassrooms.mddapi.services.TopicService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/topics")
@Validated
public class TopicController {

    private final TopicService service;

    public TopicController(TopicService service) {
        this.service = service;
    }

    // Public: list all topics
    @GetMapping
    public ResponseEntity<List<TopicResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Public: get one topic
    @GetMapping("/{id}")
    public ResponseEntity<TopicResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Authenticated users can create topics
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TopicResponseDto> create(@Valid @RequestBody TopicCreateDto dto) {
        TopicResponseDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/topics/" + created.id())).body(created);
    }

    // Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TopicResponseDto> update(@PathVariable Long id,
                                                   @Valid @RequestBody TopicUpdateDto dto) {
        TopicResponseDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
