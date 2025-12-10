package com.openclassrooms.mddapi.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.dto.subscription.SubscriptionResponseDto;
import com.openclassrooms.mddapi.services.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @PostMapping("/{topicId}")
    public ResponseEntity<SubscriptionResponseDto> subscribe(@PathVariable Long topicId) {
        return ResponseEntity.ok(service.subscribe(topicId));
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long topicId) {
        service.unsubscribe(topicId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDto>> getMySubscriptions() {
        return ResponseEntity.ok(service.getMySubscriptions());
    }
}
