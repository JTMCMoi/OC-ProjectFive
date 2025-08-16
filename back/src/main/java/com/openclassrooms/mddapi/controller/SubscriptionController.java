package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.SubscriptionResponse;
import com.openclassrooms.mddapi.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getUserSubscriptions() {
        List<SubscriptionResponse> subscriptions = subscriptionService.getUserSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long themeId) {
        subscriptionService.unsubscribe(themeId);
        return ResponseEntity.ok().build();
    }
}
